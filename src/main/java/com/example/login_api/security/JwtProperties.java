package com.example.login_api.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("security.jwt")
public class JwtProperties {
    /**
     * the secretKey si declared in the application.properties file and is used to sign the JWT, the value is in the application.properties file
     * */
    private String secretKey;
}
