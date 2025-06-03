package com.example.login_api.service;
import com.example.login_api.dto.UpdateProfileRequest;
import com.example.login_api.entity.UserEntity;
import com.example.login_api.dto.RegisterRequest;
import com.example.login_api.repository.IUserRepository;
import com.example.login_api.exception.EmailAlreadyExistsException; // Crea esta excepción personalizada
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // Esta es la parte nueva: lógica para asignar rol
        // Asignar rol de ADMIN si es el primer usuario, de lo contrario asignar USER
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

    @Transactional
    public UserEntity updateUser(Long id, UserEntity user) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Solo actualiza estos campos
        existingUser.setId(user.getId());
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


    public void deleteUser(Long id) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        userRepository.delete(existingUser);
    }

    // Lógica de negocio para actualizar el perfil de un usuario para el cliente
    public UserEntity updateProfile(String email, UpdateProfileRequest dto) {
        UserEntity existing = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // Sólo sobreescribimos los campos permitidos:
        existing.setFirstName(dto.getFirstName());
        existing.setLastName( dto.getLastName() );
        existing.setPhone(    dto.getPhone()    );
        // (no tocamos email ni password aquí)
        return userRepository.save(existing);
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
