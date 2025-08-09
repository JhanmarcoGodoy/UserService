package com.example.msuser.infrastructure.config;

import com.example.msuser.application.service.UserService;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import com.example.msuser.domain.port.service.UserServicePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {

    // Inyecta el valor de la propiedad desde application.properties
    @Value("${service.accounts.base-url}")
    private String accountsServiceUrl;

    @Bean
    public UserServicePort userServicePort(UserRepositoryPort userRepositoryPort, WebClient accountsWebClient) {
        // Ahora inyectamos el WebClient configurado en nuestro servicio
        return new UserService(userRepositoryPort, accountsWebClient);
    }

    @Bean
    public WebClient accountsWebClient(WebClient.Builder builder) {
        // Crea una instancia de WebClient pre-configurada para apuntar a ms-accounts.
        // Es una buena práctica tener un bean de WebClient por cada servicio externo.
        return builder
                .baseUrl(accountsServiceUrl)
                .build();
    }
    }