package com.example.user;

import com.example.msuser.application.service.UserService; // <-- IMPORT AÑADIDO
import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import com.example.msuser.infrastructure.adapter.web.dto.AccountDTO;
import com.example.msuser.infrastructure.adapter.web.dto.UserDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("1", "testuser", "test@example.com");
    }

    @Test
    @DisplayName("findById should return a user when user exists")
    void findById_shouldReturnUser_whenUserExists() {
        when(userRepositoryPort.findById(anyString())).thenReturn(Mono.just(testUser));

        Mono<User> resultMono = userService.findById("1");

        StepVerifier.create(resultMono)
                .expectNextMatches(user -> user.id().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("findById should return error when user does not exist")
    void findById_shouldReturnError_whenUserDoesNotExist() {
        when(userRepositoryPort.findById(anyString())).thenReturn(Mono.empty());

        Mono<User> resultMono = userService.findById("999");

        StepVerifier.create(resultMono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("findUserDetailsById should return combined details when all services succeed")
    void findUserDetailsById_shouldReturnCombinedDetails_whenAllServicesSucceed() {
        AccountDTO account1 = new AccountDTO("acc-1", "111-222", "Ahorros", new BigDecimal("1000"));
        AccountDTO account2 = new AccountDTO("acc-2", "333-444", "Corriente", new BigDecimal("500"));

        when(userRepositoryPort.findById("1")).thenReturn(Mono.just(testUser));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(any(Class.class))).thenReturn(Flux.just(account1, account2));

        Mono<UserDetailsResponse> resultMono = userService.findUserDetailsById("1");

        StepVerifier.create(resultMono)
                .expectNextMatches(details ->
                        details.id().equals("1") &&
                        details.username().equals("testuser") &&
                        details.accounts().size() == 2 &&
                        details.accounts().get(0).accountNumber().equals("111-222")
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("findUserDetailsById should return user with empty accounts list when accounts service fails")
    void findUserDetailsById_shouldReturnUserWithEmptyAccounts_whenAccountsServiceFails() {
        when(userRepositoryPort.findById("1")).thenReturn(Mono.just(testUser));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(any(Class.class))).thenReturn(Flux.error(new RuntimeException("Accounts service unavailable")));

        Mono<UserDetailsResponse> resultMono = userService.findUserDetailsById("1");

        StepVerifier.create(resultMono)
                .expectNextMatches(details ->
                        details.id().equals("1") &&
                        details.accounts().isEmpty()
                )
                .verifyComplete();
    }
}