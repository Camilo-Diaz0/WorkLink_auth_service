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

    private final PasswordResetTokenRepository repository;

    PasswordResetTokenService(PasswordResetTokenRepository repository) {
        this.repository = repository;
    }

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
        Optional<PasswordResetToken> tokenOpt = repository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return false;
        }
        PasswordResetToken resetToken = tokenOpt.get();
        if (resetToken.isUsed()) {
            return false;
        }
        return resetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    public Usuario getUserByToken(String token) {
        Optional<PasswordResetToken> tokenOpt = repository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Token inválido");
        }
        return tokenOpt.get().getUsuario();
    }

    public void invalidateToken(String token) {
        Optional<PasswordResetToken> tokenOpt = repository.findByToken(token);
        if (tokenOpt.isPresent()) {
            PasswordResetToken resetToken = tokenOpt.get();
            resetToken.setUsed(true);
            repository.save(resetToken);
        }
    }
}
