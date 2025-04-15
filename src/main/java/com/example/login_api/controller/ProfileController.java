package com.example.login_api.controller;

import com.example.login_api.dto.ProfileResponse;
import com.example.login_api.dto.UpdateProfileRequest;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS})
public class ProfileController {

    @Autowired // Inyección del repositorio
    private IUserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        String email = principal.getName();
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity userData = userOptional.get();

            // Crear y poblar el DTO
            ProfileResponse response = new ProfileResponse();
            response.setFullName(userData.getFirstName() + " " + userData.getLastName());
            response.setEmail(userData.getEmail());
            response.setRole(userData.getRole());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
    @PutMapping
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UpdateProfileRequest request) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        String email = principal.getName();
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhone(request.getPhone());

            userRepository.save(user);

            return ResponseEntity.ok("Perfil actualizado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteProfile(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        String email = principal.getName();
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return ResponseEntity.ok("Cuenta eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }


}
