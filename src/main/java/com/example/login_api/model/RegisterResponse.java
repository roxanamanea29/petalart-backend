package com.example.login_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private Long userId;  // El ID del usuario registrado
    private String message;  // Un mensaje informativo sobre el registro
    private String email;  // El email del usuario registrado
}
