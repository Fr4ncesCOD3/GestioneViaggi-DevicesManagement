package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Gestione Viaggi API")
                        .version("1.0")
                        .description("API per la gestione di viaggi e prenotazioni aziendali")
                        .contact(new Contact()
                                .name("Admin")
                                .email("admin@example.com")));
    }
} 