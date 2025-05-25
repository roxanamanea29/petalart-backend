package com.example.login_api.controller;

import com.example.login_api.dto.RegisterRequest;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")  // Solo permite acceso a ADMIN
public class AdminController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserEntity> createUserAsAdmin(@RequestBody UserEntity user) {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName(user.getFirstName());
        registerRequest.setLastName(user.getLastName());
        registerRequest.setEmail(user.getEmail());
        registerRequest.setPassword(user.getPassword());
        registerRequest.setPhone(user.getPhone());
        registerRequest.setRole(user.getRole());
        return ResponseEntity.ok(userService.registerUser(registerRequest));
    }

    // Obtener todos los usuarios (solo admins pueden acceder)
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    // Obtener un usuario por ID (solo admins pueden acceder)
    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un usuario (solo admins pueden modificar usuarios)
    @PutMapping("/user/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        try {
            UserEntity updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un usuario (solo admins pueden eliminar usuarios)
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Aseguramos que solo ADMIN pueda eliminar usuarios
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Dashboard exclusivo para administradores
    @GetMapping("/dashboard_admin")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("Bienvenido al Panel de Administración");
    }
}
