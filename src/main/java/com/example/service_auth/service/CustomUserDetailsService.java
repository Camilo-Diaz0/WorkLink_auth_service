package com.example.service_auth.service;

import com.example.service_auth.entities.UserSecurity;
import com.example.service_auth.entities.Usuario;
import com.example.service_auth.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UsuarioRepository usuarioRepository;


    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.map(UserSecurity::new)
                .orElseThrow(() -> new UsernameNotFoundException("user no encontrado; " + correo));
    }
}
