package com.hirehub.backend.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CulquiConfig {

    @Value("${culqui.secret-key}")
    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }
}
