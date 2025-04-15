package com.example.login_api.service;

import com.example.login_api.entity.UserEntity;
import com.example.login_api.model.RegisterRequest;
import com.example.login_api.repository.IUserRepository;
import com.example.login_api.exception.EmailAlreadyExistsException; // Crea esta excepción personalizada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // Método para encontrar un usuario por su email en la base de datos
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email); // Llama al repositorio para consultar la base de datos
    }

    // Método para registrar un nuevo usuario
    public UserEntity registerUser(RegisterRequest registerRequest) {
        // Verificar si el email ya está registrado
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        // Crear la entidad de usuario
        UserEntity newUser = new UserEntity();
        newUser.setFirstName(registerRequest.getFirstName());
        newUser.setLastName(registerRequest.getLastName());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setPhone(registerRequest.getPhone());

        // 👇 Esta es la parte nueva: lógica para asignar rol
        long totalUsuarios = userRepository.count();
        if (totalUsuarios == 0) {
            newUser.setRole("ROLE_ADMIN");
        } else {
            newUser.setRole("ROLE_USER");
        }

        // Guardar el nuevo usuario en la base de datos
        return userRepository.save(newUser);
    }


    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public UserEntity updateUser(Long id, UserEntity user) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Solo actualiza estos campos
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        // Si la contraseña es null, mantiene la anterior
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(existingUser);
    }


    public ResponseEntity<String> deleteUser(Long id) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        userRepository.delete(existingUser);
        return ResponseEntity.ok("usuario eliminado");
    }
    // Lógica de negocio para actualizar el perfil de un usuario para el cliente
    public UserEntity updateProfile(String email, UserEntity updatedUser) {
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(email);

        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());  // Si es necesario, puedes encriptar la contraseña
            existingUser.setPhone(updatedUser.getPhone());
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    //logica para eliminar el perfil de un usuario
    public ResponseEntity<String> deleteProfile(String email) {
        Optional<UserEntity> existingUserOptional = userRepository.findByEmail(email);

        if (existingUserOptional.isPresent()) {
            UserEntity existingUser = existingUserOptional.get();
            userRepository.delete(existingUser);
            return ResponseEntity.ok("usuario eliminado");
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
