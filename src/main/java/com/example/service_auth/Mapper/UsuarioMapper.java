package com.example.service_auth.Mapper;

import com.example.service_auth.dto.Response.UsuarioResponse;
import com.example.service_auth.entities.Usuario;

public class UsuarioMapper {

    public static UsuarioResponse toDTO(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        UsuarioResponse response = new UsuarioResponse();
        
        response.setEmail(
            usuario.getCorreo()
        );
        response.setNombre(
            usuario.getNombre()
        );
        response.setApellido(
            usuario.getApellido()
        );
        response.setTelefono(
            usuario.getTelefono()
        );

        return response;
    }

    public static Usuario toEntity(UsuarioResponse response) {

        if (response == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setCorreo(
            response.getEmail()
        );
        usuario.setNombre(
            response.getNombre()
        );
        usuario.setApellido(
            response.getApellido()
        );
        usuario.setTelefono(
            response.getTelefono()
        );

        return usuario;
    }

}
