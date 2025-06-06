package com.example.login_api.security;

import com.example.login_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    /**
     *Este método se usa para cargar los detalles del usuario desde la base de datos.
     * Se utiliza en el proceso de autenticación para verificar las credenciales del usuario al hacer login.
     *
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("🛠 Construyendo UserPrincipal: ID=" + user.getId() + ", Email=" + user.getEmail());  // Depuración
        // Crear un objeto UserPrincipal a partir del usuario encontrado
        return UserPrincipal.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))//Es un objeto de Spring que representa el rol de un usuario
                .password(user.getPassword())
                .build();
    }
}
