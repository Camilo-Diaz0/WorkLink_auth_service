package com.example.service_auth.dto;

public class ChangePasswordDto {
    private String correo;
    private String password;
    private  String token;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String correo) {
        this.correo = correo;
    }

    public ChangePasswordDto(String password, String token) {
        this.password = password;
        this.token = token;
    }

    public ChangePasswordDto(String correo, String password, String token) {
        this.correo = correo;
        this.password = password;
        this.token = token;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
