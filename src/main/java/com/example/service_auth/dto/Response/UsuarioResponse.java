package com.example.service_auth.dto.Response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsuarioResponse {

    @JsonProperty("email")
    private String email;
    
    @JsonProperty("nombre")
    private String nombre;
    
    @JsonProperty("apellido")
    private String apellido;
    
    @JsonProperty("telefono")
    private String telefono;
    
    @JsonProperty("fotoPerfilUrl")
    private String fotoPerfilUrl;
    
    @JsonProperty("fechaNacimiento")
    private LocalDate fechaNacimiento;
    
    public UsuarioResponse() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String mensajeError(String mensaje) {
        return mensaje;
    }

    public String mensajeExito(String mensaje) {
        return mensaje;
    }

    public String mensajeBienvenida(String mensaje) {
        return mensaje;
    }

}
