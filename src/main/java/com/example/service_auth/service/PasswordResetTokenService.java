package com.example.service_auth.service;

import com.example.service_auth.entities.PasswordResetToken;
import com.example.service_auth.entities.Usuario;
import com.example.service_auth.repositories.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository repository;

    public PasswordResetToken saveToken(Usuario usuario, String token) {
        //verificar que no haya un token activo
        Optional<PasswordResetToken> tokenActivo = repository.findByUsuarioAndUsedFalseAndExpiryDateAfter(usuario, LocalDateTime.now());

        if(tokenActivo.isPresent()){
            return null;
        }

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // válido 1 hora
        return repository.save(resetToken);
    }

    public boolean isValidToken(String token) {
        return repository.findByToken(token)
                .filter(t -> !t.isUsed() && t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    public Usuario getUserByToken(String token) {
        return repository.findByToken(token)
                .map(PasswordResetToken::getUsuario)
                .orElseThrow(() -> new RuntimeException("Token inválido"));
    }

    public void invalidateToken(String token) {
        repository.findByToken(token).ifPresent(t -> {
            t.setUsed(true);
            repository.save(t);
        });
    }
}
