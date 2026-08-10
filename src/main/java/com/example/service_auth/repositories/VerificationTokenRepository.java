package com.example.service_auth.repositories;

import com.example.service_auth.entities.Usuario;
import com.example.service_auth.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUsuarioAndUsedFalseAndExpiryDateAfter(Usuario usuario, LocalDateTime now);
}