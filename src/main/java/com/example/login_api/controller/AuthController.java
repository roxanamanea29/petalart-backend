package com.example.login_api.controller;


import com.example.login_api.model.LoginRequest;
import com.example.login_api.model.LoginResponse;
import com.example.login_api.security.JwtIssuer;
import com.example.login_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor

public class
AuthController {

    private final AuthService authService;//maneja la logica de de autenticacion ,verificacion de credenciales y generacion de token
    private final JwtIssuer jwtIssuer;//maneja la generacion de token
    private final AuthenticationManager authenticationManager;//maneja la autenticacion utilizando el usuario y contraseña

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

}
