package com.hirehub.backend.user.dto;

public record RegisterRequestDTO(
    String name,
    String email,
    String phone,
    String password,
    String role
) {}
