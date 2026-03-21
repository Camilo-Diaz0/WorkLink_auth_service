package com.example.service_auth.controllers;

import com.example.service_auth.dto.ChangePasswordRequest;
import com.example.service_auth.dto.PasswordResetRequest;
import com.example.service_auth.dto.RegistroRequest;
import com.example.service_auth.entities.PasswordResetToken;
import com.example.service_auth.entities.Usuario;
import com.example.service_auth.service.PasswordResetTokenService;
import com.example.service_auth.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
@CrossOrigin(originPatterns = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordResetTokenService tokenService;

    public static Logger logger = Logger.getAnonymousLogger();

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Optional<Usuario> optionalUsuario = usuarioService.buscar(id);
        if (optionalUsuario.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(optionalUsuario.get());

    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody RegistroRequest usuarioDto){

        Usuario save = usuarioService.registrar(usuarioDto);
        if(save == null)  return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(save);

    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('cliente')")
    public ResponseEntity<String> mensaje(){
        return ResponseEntity.ok("Logeado correctamente");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody PasswordResetRequest dto){
        String correo = dto.getCorreo();
        Optional<Usuario> usuario = usuarioService.buscarPorCorreo(correo);
        if(usuario.isEmpty()){
            return ResponseEntity.ok("Si el correo esta registrado, se enviara un enlace");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken guardado = tokenService.saveToken(usuario.get(), token);
        if (guardado == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya tiene un token activo");


        return ResponseEntity.ok("Se ha enviado un enlace de recuperacion de contraseña "+token);
    }

    @PostMapping("/new-password")
    public ResponseEntity<String> editPassword(@Valid @RequestBody ChangePasswordRequest dto){
        if(!tokenService.isValidToken(dto.getToken())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido o expirado");
        }
        Usuario usuario = tokenService.getUserByToken(dto.getToken());
        tokenService.invalidateToken(dto.getToken());
        usuario = usuarioService.cambiarPassword(dto.getPassword(), usuario);
        if (usuario == null) ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo actualizar la contraseña");
        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }

}

