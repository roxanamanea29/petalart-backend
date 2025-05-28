package com.example.login_api.security;


import com.example.login_api.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Clase que implementa UserDetails para representar al usuario autenticado en Spring Security.
 * Contiene información del usuario como ID, email, nombre, apellidos y roles.
 * También proporciona métodos necesarios para la autenticación y autorización.
 */
@Getter
@Builder
public class UserPrincipal implements UserDetails {//UserDetails es una interfaz de Spring Security que representa los detalles del usuario autenticados
    // Atributos del usuario autenticado
    private final Long userId;

    private final String email;

    @JsonIgnore
    private final String password;

    private final String firstName;

    private final String lastName;

    //los roles del usuario que se asignan como autoridades
    private  final Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }


//metodo que convierte el UserPrincipal a una entidad UserEntity
    public UserEntity getUser() {
        UserEntity user = new UserEntity();
        user.setId(this.userId);
        user.setEmail(this.email);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setPassword(this.password);
        // Set other necessary fields
        return user;
    }


}
