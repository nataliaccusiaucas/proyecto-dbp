package com.hirehub.backend.payment.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CulqiConfig {

    @Value("${CULQUI_SECRET_KEY}")
    private String secretKey;
}



