package com.example.login_api.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
/**
 * es el DTO para actualizar la información del usuario.
 * Contiene los campos que se pueden actualizar como nombre, apellido, teléfono, email, rol y contraseña.
 * **/
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String role;
    private String password;
}
