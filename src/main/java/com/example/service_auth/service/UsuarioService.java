package com.example.service_auth.service;

import com.example.service_auth.dto.RegistroRequest;
import com.example.service_auth.entities.Usuario;
import com.example.service_auth.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
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
    public Usuario registrar(RegistroRequest dto){
        Usuario usuario = new Usuario(
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getPassword(),
                dto.getTelefono(),
                dto.getRol()
        );
        return save(usuario);
    }

    public Optional<Usuario> buscar(Long id){
        return repository.findById(id);
    }

    public Optional<Usuario>buscarPorCorreo(String correo) {
        return repository.findByCorreo(correo);
    }

    public Usuario cambiarPassword(String password, Usuario usuario){
        password = passwordEncoder.encode(password);
        usuario.setPassword(password);
        usuario.setUpdate_at(LocalDateTime.now());
        usuario = repository.save(usuario);
        if(usuario.getPassword().equals(password)) return usuario;
        return null;
    }
}
