package com.example.msuser.infrastructure.adapter.web.dto;

import java.math.BigDecimal;

// Representa la estructura de una cuenta que recibimos de ms-accounts.
public record AccountDTO(
    String id,
    String accountNumber,
    String accountType,
    BigDecimal balance
) {}