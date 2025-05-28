package com.example.login_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
/**
 * 🔐 Filtro de autenticación JWT que se ejecuta una sola vez por solicitud.
 * Se encarga de extraer, decodificar y validar el JWT recibido por cabecera.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extraer token del header Authorization (si existe)
        Optional<String> tokenOpt = extractTokenFromRequest(request);

        // Si no hay token, continuar sin autenticación (permite rutas públicas)
        if (tokenOpt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Decodificar y validar token
            var decodedJWT = jwtDecoder.decode(tokenOpt.get());

            // 4Convertir claims del token a un principal personalizado
            var principal = jwtToPrincipalConverter.convert(decodedJWT);

            //  Crear objeto de autenticación y asignarlo al contexto de seguridad
            var authentication = new UserPrincipalAuthenticationToken(principal);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            //  Si el token es inválido, no lanza error pero continúa sin autenticación
            System.out.println(" Token inválido o error en autenticación: " + e.getMessage());
        }

        //  Continuar con el resto de filtros (sea válido o no el token)
        filterChain.doFilter(request, response);
    }
    /**
     * Extrae el token JWT del header Authorization en formato Bearer.
     *//**/
    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7)); // "Bearer " tiene 7 caracteres
        }
        return Optional.empty();
    }
}
