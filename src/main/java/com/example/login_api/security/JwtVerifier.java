package com.example.login_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtVerifier {

    private final JwtProperties properties;

    public boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
                    .build()
                    .verify(token);
            return true; // Token is valid
        } catch (TokenExpiredException e) {
            // Handle token expiration
            System.out.println("Token has expired");
        } catch (JWTVerificationException e) {
            // Handle token verification failure
            System.out.println("Invalid token");
        }
        return false;
    }
}