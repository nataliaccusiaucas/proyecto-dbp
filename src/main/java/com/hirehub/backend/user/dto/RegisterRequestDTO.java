package com.hirehub.backend.user.dto;

import com.hirehub.backend.user.domain.Role;

public record RegisterRequestDTO(
    String name,
    String email,
    String phone,
    String password,
    Role role
) {}