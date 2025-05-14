package com.example.login_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtDecoder {
    private final JwtProperties properties;
    /**
     * Decodifica el token JWT y devuelve el objeto DecodedJWT.
     * @param token el token JWT a decodificar
     * @devuelve el objeto DecodedJWT que contiene la información decodificada del token
     * @throws JWTDecodeException si el token es inválido o no se puede decodificar
     */
    public DecodedJWT decode(String token) {
        // Validar que el token que no esté vacío o mal formado
        if (token == null || token.trim().isEmpty()) {
            throw new JWTDecodeException("Token inválido o vacío");
        }
        try {
            return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new JWTDecodeException("Error al decodificar el token: " + e.getMessage());
        }
    }
}
