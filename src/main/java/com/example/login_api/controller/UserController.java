package com.example.login_api.controller;

import com.example.login_api.entity.UserEntity;
import com.example.login_api.exception.EmailAlreadyExistsException;
import com.example.login_api.model.RegisterRequest;
import com.example.login_api.model.RegisterResponse;
import com.example.login_api.repository.IUserRepository;
import com.example.login_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final IUserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            var user = userService.registerUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new RegisterResponse(user.getId(), "Usuario registrado con éxito", user.getEmail())
            );
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new RegisterResponse(null, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Bienvenido al Dashboard de Usuario");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        String email = principal.getName();
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            UserEntity userData = user.get();

            Map<String, Object> profileData = new HashMap<>();
            profileData.put("name", userData.getFirstName() + " " + userData.getLastName());
            profileData.put("email", userData.getEmail());
            profileData.put("role", userData.getRole());

            return ResponseEntity.ok(profileData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<UserEntity> updateProfile(Principal principal, @RequestBody UserEntity updatedUser) {
        String email = principal.getName();
        UserEntity user = userService.updateProfile(email, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteProfile(Principal principal) {
        String email = principal.getName();
        return userService.deleteProfile(email);
    }
}
