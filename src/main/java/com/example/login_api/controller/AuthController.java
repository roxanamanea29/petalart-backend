package com.example.login_api.controller;


import com.example.login_api.exception.EmailAlreadyExistsException;
import com.example.login_api.dto.LoginRequest;
import com.example.login_api.dto.LoginResponse;
import com.example.login_api.dto.RegisterResponse;
import com.example.login_api.security.JwtIssuer;
import com.example.login_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class
AuthController {

    private final AuthService authService;//maneja la logica de de autenticacion ,verificacion de credenciales y generacion de token
    private final JwtIssuer jwtIssuer;//maneja la generacion de token
    private final AuthenticationManager authenticationManager;//maneja la autenticacion utilizando el usuario y contraseña

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest request) {
        try {
            LoginResponse response = authService.attemptLogin(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponse(null, e.getMessage(), null));
        }
    }
}
