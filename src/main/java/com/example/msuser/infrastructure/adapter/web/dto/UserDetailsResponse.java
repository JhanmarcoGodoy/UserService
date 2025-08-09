package com.example.msuser.infrastructure.adapter.web.dto;

import java.util.List;


public record UserDetailsResponse(
    String id,
    String username,
    String email,
    List<AccountDTO> accounts 
) {}