package com.example.login_api.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {
    @Value("${SENDGRID_API_KEY:SG.dev.abc0123456789}")
    private String apiKey;

    @Bean
    public SendGrid sendGridClient() {
        return new SendGrid(apiKey);
    }
}
