package com.example.service_auth.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.entities.Usuario;
import com.example.service_auth.repositories.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository repository;

    public AuthService(JwtService jwtService, AuthenticationManager authenticationManager, UsuarioRepository repository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.repository = repository;
    }

    public String crearToken(AuthRequest authRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getCorreo(), authRequest.getPassword()
            )
        );

        Optional<Usuario> usuarioOpt = repository.findByCorreo(authRequest.getCorreo());
        if (usuarioOpt.isEmpty()) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.isActivo()) {
            throw new DisabledException("Usuario inactivo");
        }
        
        if (!usuario.isVerificado()) {
            throw new DisabledException("Cuenta no verificada");
        }

        return jwtService.generarToken(usuario);
    }
}