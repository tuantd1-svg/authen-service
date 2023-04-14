package com.example.authenservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiService {
    @Value("${url.core-service.baseUrl}")
    private String baseUrlAccount;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Bean("webClientServiceCoreAccount")
    public WebClient webClientServiceAccount() {
        return webClientBuilder.baseUrl(baseUrlAccount).build();
    }
}
