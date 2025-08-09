package com.example.msuser.infrastructure.config;

import com.example.msuser.application.service.UserService;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import com.example.msuser.domain.port.service.UserServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public UserServicePort userServicePort(UserRepositoryPort userRepositoryPort) {
        return new UserService(userRepositoryPort);
    }
}