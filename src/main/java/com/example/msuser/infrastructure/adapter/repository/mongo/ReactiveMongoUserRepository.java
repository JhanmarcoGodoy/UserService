package com.example.msuser.infrastructure.adapter.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveMongoUserRepository extends ReactiveMongoRepository<MongoUserEntity, String> {

}