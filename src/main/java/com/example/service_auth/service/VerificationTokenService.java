package com.example.service_auth.service;

import com.example.service_auth.entities.Usuario;
import com.example.service_auth.entities.VerificationToken;
import com.example.service_auth.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository repository;
    private final long expirationHours;

    public VerificationTokenService(VerificationTokenRepository repository,
                                    @Value("${app.verification.expiration-hours:24}") long expirationHours) {
        this.repository = repository;
        this.expirationHours = expirationHours;
    }

    public String generarToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUsuario(usuario);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(expirationHours));

        repository.save(verificationToken);
        return token;
    }

    public VerificationToken obtenerValido(String token) {
        Optional<VerificationToken> tokenOpt = repository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            return null;
        }

        VerificationToken verificationToken = tokenOpt.get();
        if (verificationToken.isUsed()) {
            return null;
        }
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return null;
        }

        return verificationToken;
    }

    public void marcarUsado(VerificationToken verificationToken) {
        verificationToken.setUsed(true);
        repository.save(verificationToken);
    }
}
