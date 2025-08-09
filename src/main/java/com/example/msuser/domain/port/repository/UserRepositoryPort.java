package com.example.msuser.domain.port.repository;

import com.example.msuser.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface UserRepositoryPort {
    Flux<User> findAll();
    Mono<User> findById(String id);
    Mono<User> save(User user);
    Mono<Void> deleteById(String id);
}