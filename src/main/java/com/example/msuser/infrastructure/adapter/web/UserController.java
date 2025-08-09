package com.example.msuser.infrastructure.adapter.web;

import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.service.UserServicePort;
import com.example.msuser.infrastructure.adapter.web.dto.UserRequest;
import com.example.msuser.infrastructure.adapter.web.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServicePort userServicePort;

    public UserController(UserServicePort userServicePort) {
        this.userServicePort = userServicePort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> createUser(@RequestBody UserRequest request) {
        User user = new User(null, request.username(), request.email());
        return userServicePort.createUser(user).map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> getUserById(@PathVariable String id) {
        return userServicePort.findById(id).map(this::toResponse);
    }

    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        return userServicePort.findAll().map(this::toResponse);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.id(), user.username(), user.email());
    }
}