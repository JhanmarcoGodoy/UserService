package com.example.msuser.domain.port.service;

import com.example.msuser.domain.model.User;
import com.example.msuser.infrastructure.adapter.web.dto.UserDetailsResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserServicePort {
    Flux<User> findAll();
    Mono<User> findById(String id);
    Mono<User> createUser(User user);
    Mono<Void> deleteById(String id);
    Mono<UserDetailsResponse> findUserDetailsById(String id);
}