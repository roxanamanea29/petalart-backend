package com.example.login_api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;  // Se inyecta el PasswordEncoder como bean separado

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //  Agregamos CORS correctamente para frontend desde React (5173)
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:5173")); // ✅ Solo React
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Permitir REST
            config.setAllowedHeaders(List.of("*")); // ✅ Permitir headers comunes como Authorization, Content-Type
            config.setAllowCredentials(true); // ✅ Permitir cookies y tokens
            return config;
        }));

        //  Desactivamos CSRF (porque se usa JWT)
        http.csrf(csrf -> csrf.disable());

        //  Seguridad por rutas
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/with-products/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/cart/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/order/create").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/auth/dashboard").hasRole("USER")
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .requestMatchers("/auth/profile").authenticated()
                                .requestMatchers("/payment/**").authenticated()
                                .requestMatchers("/address/**").authenticated()
/*
                        // 🛡️ Solo admin puede crear/editar/borrar
                        .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")*/

                        //
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults());

        // 🔐 Filtro personalizado JWT
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder);  // Se pasa directamente el bean PasswordEncoder
        return authenticationManagerBuilder.build();
    }
}
