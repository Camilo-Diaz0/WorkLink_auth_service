package com.example.service_auth.controllers;

import com.example.service_auth.entities.Usuario;
import com.example.service_auth.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(@PathVariable Long id){
        Optional<Usuario> optionalUsuario = service.buscar(id);
        if (optionalUsuario.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(optionalUsuario.get());

    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario){
        if(usuario.getId() != null ) return ResponseEntity.badRequest().build();

        Usuario save = service.save(usuario);
        if(save == null)  return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(save);

    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('cliente')")
    public ResponseEntity<String> mensaje(){
        return ResponseEntity.ok("Logeado correctamente");
    }

}

