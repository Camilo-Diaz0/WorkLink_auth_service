package com.example.service_auth.repositories;

import com.example.service_auth.entities.PasswordResetToken;
import com.example.service_auth.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsuarioAndUsedFalseAndExpiryDateAfter(Usuario usuario, LocalDateTime now);
}
