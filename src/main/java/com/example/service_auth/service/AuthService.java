package com.example.service_auth.service;

import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioRepository repository;

    public String crearToken(AuthRequest authRequest) throws BadCredentialsException {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getCorreo(),
                authRequest.getPassword()
        ));

        return jwtService.generarToken(authRequest.getCorreo());

    }
}
