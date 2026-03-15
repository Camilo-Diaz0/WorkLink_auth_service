package com.example.service_auth.entities;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserSecurity implements UserDetails {
    private String correo;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserSecurity(Usuario usuario) {
        correo = usuario.getCorreo();
        password = usuario.getPassword();
        authorities = Arrays.stream(
                usuario.getRol().split(",")
        )
                .map(rol -> new SimpleGrantedAuthority("ROLE_"+rol))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
