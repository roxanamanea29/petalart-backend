package com.example.login_api.service;


import com.example.login_api.dto.LoginResponse;
import com.example.login_api.security.JwtIssuer;
import com.example.login_api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/*
* clase de servicio que maneja la logica de autenticación
* */
@Service
@RequiredArgsConstructor
public class AuthService {

    //se inyectan las dependencias
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    //metodo para realizar el login  pasando el email y la contraseña
    public LoginResponse attemptLogin(String email, String password) {
        // verifica los datos
        var authentication = authenticationManager.authenticate(
                // crea un objeto de autenticación con el email y la contraseña
                new UsernamePasswordAuthenticationToken(email, password)
        );
        // si la autenticación se realiza
        System.out.println(" Autenticación realizada con el email: " + email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(" Contexto de seguridad configurado para: " + email);
        // obtiene el principal de la autenticacion que es el usario autenticado
        var principal = (UserPrincipal) authentication.getPrincipal();

        //aqui se obuscan los roles del usario autenticado
        var roles = principal.getAuthorities().stream()
                //se obtiene los roles del usuario
                .map(GrantedAuthority::getAuthority)
                //se trnasforma en una lista de roles
                .toList();
        //se genera el token con el issuer que es el encargado de genrar un token  con los datos id, email y rol
        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

       /* System.out.println(" Token JWT generado: " + token);*/
        // se imprime el nombre completo del usuario
        String fullName = principal.getFirstName() + " " + principal.getLastName();
        System.out.println("🛠 Nombre completo del usuario: " + fullName);
        return LoginResponse.builder()
                .accessToken(token)
                .roles(roles)
                .userId(principal.getUserId())
                .email(principal.getEmail())
                .name(fullName)
                .build();
    }
}
