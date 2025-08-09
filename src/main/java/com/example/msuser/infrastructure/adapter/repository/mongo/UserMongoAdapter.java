package com.example.msuser.infrastructure.adapter.repository.mongo;

import com.example.msuser.domain.model.User;
import com.example.msuser.domain.port.repository.UserRepositoryPort;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserMongoAdapter implements UserRepositoryPort {

    private final ReactiveMongoUserRepository mongoRepository;

    public UserMongoAdapter(ReactiveMongoUserRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }


    @Override
    public Flux<User> findAll() {
        return mongoRepository.findAll().map(this::toDomainModel);
    }

    @Override
    public Mono<User> findById(String id) {
        return mongoRepository.findById(id).map(this::toDomainModel);
    }

    @Override
    public Mono<User> save(User user) {
        MongoUserEntity entity = toEntity(user);
        return mongoRepository.save(entity).map(this::toDomainModel);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongoRepository.deleteById(id);
    }


    private User toDomainModel(MongoUserEntity entity) {
        return new User(entity.id(), entity.username(), entity.email());
    }

    private MongoUserEntity toEntity(User user) {
        return new MongoUserEntity(user.id(), user.username(), user.email());
    }
}