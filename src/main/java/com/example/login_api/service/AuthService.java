package com.example.login_api.service;


import com.example.login_api.model.LoginResponse;
import com.example.login_api.security.JwtIssuer;
import com.example.login_api.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    public LoginResponse attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        System.out.println(" Autenticación realizada para: " + email);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("🛠 Contexto de seguridad configurado para: " + email);

        var principal = (UserPrincipal) authentication.getPrincipal();

        var roles = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        System.out.println("🛠 Token JWT generado: " + token);

        return LoginResponse.builder()
                .accessToken(token)
                .roles(roles)
                .userId(principal.getUserId())
                .build();
    }
}
