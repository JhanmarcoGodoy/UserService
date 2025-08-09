package com.example.msuser.application.service;

import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import com.example.msuser.domain.port.service.UserServicePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Esta clase NO lleva anotación @Service. La crearemos como un Bean en una clase de configuración
// para desacoplar completamente la lógica de aplicación del framework.
public class UserService implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;

    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Flux<User> findAll() {
        return userRepositoryPort.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepositoryPort.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado con id: " + id))); // Puedes crear una excepción personalizada
    }

    @Override
    public Mono<User> createUser(User user) {
        // Aquí podrías añadir lógica de negocio, como validar que el email no exista, etc.
        return userRepositoryPort.save(user);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return userRepositoryPort.deleteById(id);
    }
}