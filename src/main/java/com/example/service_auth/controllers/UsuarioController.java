package com.example.service_auth.controllers;

import com.example.service_auth.Mapper.UsuarioMapper;
import com.example.service_auth.dto.ChangePasswordRequest;
import com.example.service_auth.dto.PasswordResetRequest;
import com.example.service_auth.dto.Request.RegistroRequest;
import com.example.service_auth.entities.PasswordResetToken;
import com.example.service_auth.entities.Usuario;
import com.example.service_auth.entities.VerificationToken;
import com.example.service_auth.service.EmailService;
import com.example.service_auth.service.PasswordResetTokenService;
import com.example.service_auth.service.UsuarioService;
import com.example.service_auth.service.VerificationTokenService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final PasswordResetTokenService tokenService;
    private final VerificationTokenService verificationTokenService;
	private final EmailService emailService;

    public UsuarioController(UsuarioService usuarioService, PasswordResetTokenService tokenService, VerificationTokenService verificationTokenService, EmailService emailService) {
        this.tokenService = tokenService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.usuarioService = usuarioService; 
    }

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrar(@Valid @RequestBody RegistroRequest dto) {
        Usuario save = usuarioService.registrar(dto);
        if (save == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo registrar el usuario"));
        }

        String token = verificationTokenService.generarToken(save);
        try {
            emailService.enviarVerificacion(save.getCorreo(), token);
        } catch (MailException e) {
            // La cuenta queda creada; el usuario podrá solicitar el reenvío del correo
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of("mensaje", "Registro exitoso. Revisa tu correo para verificar la cuenta.")
            );
    }


    // Antes: GET /user/{id} público -> enumeración. Ahora solo el usuario autenticado ve lo suyo.
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('cliente','proveedor')")
    public ResponseEntity<?> currentUser(@AuthenticationPrincipal String correo) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
            UsuarioMapper.toDTO(
                usuarioOpt.get()
            )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetRequest dto) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorCorreo(
            dto.getCorreo()
        );
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String token = UUID.randomUUID().toString();
            PasswordResetToken guardado = tokenService.saveToken(usuario, token);

            // saveToken devuelve null si el usuario ya tiene un token activo: no se reenvia
            if (guardado != null) {
                try {
                    emailService.enviarRecuperacion(usuario.getCorreo(), token);
                } catch (MailException e) {
                    // Se responde igual para no revelar si el correo esta registrado
                }
            }
        }

        return ResponseEntity.ok("Si el correo está registrado, se enviará un enlace de recuperación");
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> editPassword(@Valid @RequestBody ChangePasswordRequest dto) {
        if (!tokenService.isValidToken(dto.getToken())) {
            return ResponseEntity.status(401).body("Token inválido o expirado");
        }

        Usuario usuario = tokenService.getUserByToken(
            dto.getToken()
        );
        tokenService.invalidateToken(
            dto.getToken()
        );
        usuarioService.cambiarPassword(dto.getPassword(), usuario);
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

    @GetMapping("/verificar")
    public ResponseEntity<Map<String, String>> verificar(@RequestParam String token) {
        VerificationToken vt = verificationTokenService.obtenerValido(token);
        if (vt == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Token de verificación inválido o expirado")
            ); 
        }

        usuarioService.marcarVerificado(vt.getUsuario());
        verificationTokenService.marcarUsado(vt);
        return ResponseEntity.ok(
            Map.of("mensaje", "Cuenta verificada. Ya puedes iniciar sesión.")
        );
    }

}