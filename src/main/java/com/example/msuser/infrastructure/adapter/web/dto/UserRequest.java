package com.example.msuser.infrastructure.adapter.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

// DTO para peticiones entrantes de creación.
// No tiene 'id' porque el usuario aún no existe.
// Usamos records por su inmutabilidad y concisión.
public record UserRequest(
    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
    String username,

    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    String email
) {}