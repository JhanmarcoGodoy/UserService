package com.example.msuser.infrastructure.adapter.repository.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public record MongoUserEntity(
    @Id String id,
    String username,
    String email
) {}