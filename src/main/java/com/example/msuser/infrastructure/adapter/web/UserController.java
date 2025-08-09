package com.example.msuser.infrastructure.adapter.web;

import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.service.UserServicePort;
import com.example.msuser.infrastructure.adapter.web.dto.UserDetailsResponse;
import com.example.msuser.infrastructure.adapter.web.dto.UserRequest;
import com.example.msuser.infrastructure.adapter.web.dto.UserResponse;
import jakarta.validation.Valid;
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

    // --- Endpoints existentes (con su implementación completa) ---

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        // Llama al servicio y mapea el resultado al DTO de respuesta
        return userServicePort.createUser(toDomainModel(request))
                .map(this::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<UserResponse> getUserById(@PathVariable String id) {
        // Llama al servicio y mapea el resultado al DTO de respuesta
        return userServicePort.findById(id)
                .map(this::toResponse);
    }

    @GetMapping
    public Flux<UserResponse> getAllUsers() {
        // Llama al servicio y mapea cada elemento del flujo al DTO de respuesta
        return userServicePort.findAll()
                .map(this::toResponse);
    }

    // --- ¡NUEVO ENDPOINT DE AGREGACIÓN! ---

    @GetMapping("/{id}/details")
    public Mono<UserDetailsResponse> getUserDetails(@PathVariable String id) {
        return userServicePort.findUserDetailsById(id);
    }

    // --- Mapeadores privados ---

    private UserResponse toResponse(User user) {
        return new UserResponse(user.id(), user.username(), user.email());
    }

    private User toDomainModel(UserRequest request) {
        return new User(null, request.username(), request.email());
    }
}