package com.example.service_auth.controllers;

import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("")
public class AuthController {

    final AuthService service;

    AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticar(@Valid @RequestBody AuthRequest authRequest) {
        try {
            String token = service.crearToken(authRequest);
            return ResponseEntity.ok(
                Map.of("token", token)
            );
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Cuenta no verificada. Revisa tu correo.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o Contraseña incorrecta");
        }
    }
}
