package com.sistema.notas.config.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    // Atributos personalizados para la arquitectura
    private final Integer id;
    private final String email;
    private final String password;
    private final Integer tenantId;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean active;

    public CustomUserDetails(
            Integer id,
            String email,
            String password,
            Integer tenantId,
            String role,
            Collection<? extends GrantedAuthority> authorities,
            boolean active
    ) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.tenantId = tenantId;
        this.role = role;
        this.authorities = authorities;
        this.active = active;
    }

    // --- Métodos requeridos por la interfaz UserDetails ---

    @Override
    public String getUsername() {
        return this.email; 
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}