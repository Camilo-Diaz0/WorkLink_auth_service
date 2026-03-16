package com.example.service_auth.controllers;

import com.example.service_auth.dto.AuthRequest;
import com.example.service_auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController("")
public class AuthController {

    @Autowired
    AuthService service;

    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@RequestBody AuthRequest authRequest){
        try{
            String token = service.crearToken(authRequest);
            return ResponseEntity.ok(token);
        }  catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Correo o Contraseña incorrecta");

        }
    }
}
