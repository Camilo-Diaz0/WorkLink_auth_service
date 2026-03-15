package com.example.service_auth.service;

import com.example.service_auth.entities.Usuario;
import com.example.service_auth.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Usuario save(Usuario usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario save = repository.save(usuario);
        if(save.getId() != null) return save;
        return null;

    }

    public Optional<Usuario> buscar(Long id){
        return repository.findById(id);
    }
}
