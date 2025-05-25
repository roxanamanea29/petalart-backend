package com.example.login_api.controller;


import com.example.login_api.dto.*;
import com.example.login_api.exception.EmailAlreadyExistsException;
import com.example.login_api.security.JwtIssuer;
import com.example.login_api.service.AuthService;
import com.example.login_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/*
* Este controller maneja el login y registrio de usuarios público.
* Endpoints:
		POST /auth/login → Inicia sesión.
		POST /auth/register → Registrar un nuevo usuario
* */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class
AuthController {
    private final UserService userService;//maneja la logica de negocio de los usuarios

    private final AuthService authService;//maneja la logica de de autenticacion ,verificacion de credenciales y generacion de token
    private final JwtIssuer jwtIssuer;//maneja la generacion de token
    private final AuthenticationManager authenticationManager;//maneja la autenticacion utilizando el usuario y contraseña

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginRequest request) {
        try {
            LoginResponse response = authService.attemptLogin(request.getEmail(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegisterResponse(null, e.getMessage(), null));
        }
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            var user = userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new UserResponse(
                            user.getId(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getRole(),
                            user.getCreatedAt(),
                            user.getUpdatedAt()
                    )
            );
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}