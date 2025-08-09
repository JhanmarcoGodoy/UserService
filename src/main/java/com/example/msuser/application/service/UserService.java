package com.example.msuser.application.service;

import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import com.example.msuser.domain.port.service.UserServicePort;
import com.example.msuser.infrastructure.adapter.web.dto.AccountDTO;
import com.example.msuser.infrastructure.adapter.web.dto.UserDetailsResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;
    private final WebClient webClient;

    public UserService(UserRepositoryPort userRepositoryPort, WebClient webClient) {
        this.userRepositoryPort = userRepositoryPort;
        this.webClient = webClient;
    }

    @Override
    public Flux<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con id: " + id)));
    }

    @Override
    public Mono<User> createUser(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepositoryPort.deleteById(id);
    }

    @Override
    public Mono<UserDetailsResponse> findUserDetailsById(String id) {
        Mono<User> userMono = this.findById(id);

        Mono<List<AccountDTO>> accountsMono = webClient.get()
                .uri("/accounts/user/{userId}", id)
                .retrieve()
                .bodyToFlux(AccountDTO.class)
                .collectList()
                .onErrorReturn(List.of());

        return Mono.zip(userMono, accountsMono)
                .map(tuple -> new UserDetailsResponse(
                        tuple.getT1().id(),
                        tuple.getT1().username(),
                        tuple.getT1().email(),
                        tuple.getT2()
                ));
    }
}