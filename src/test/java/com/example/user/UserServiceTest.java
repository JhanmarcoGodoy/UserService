package com.example.user;

import com.example.msuser.application.service.UserService;
import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("1", "testuser", "test@example.com");
    }

    @Test
    @DisplayName("Debe devolver un usuario cuando se encuentra por ID (Happy Path)")
    void findById_shouldReturnUser_whenUserExists() {
        when(userRepositoryPort.findById(anyString()))
                .thenReturn(Mono.just(testUser));

        Mono<User> resultMono = userService.findById("1");

        StepVerifier.create(resultMono)
                .expectNextMatches(foundUser ->
                        foundUser.id().equals("1") &&
                        foundUser.username().equals("testuser")
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe devolver un error cuando el usuario no se encuentra por ID")
    void findById_shouldReturnError_whenUserDoesNotExist() {
        when(userRepositoryPort.findById(anyString())).thenReturn(Mono.empty());

        Mono<User> resultMono = userService.findById("999");

        StepVerifier.create(resultMono)
                .expectError(RuntimeException.class)
                .verify();
    }
}
