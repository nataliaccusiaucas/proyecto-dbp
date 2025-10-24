package com.hirehub.backend.user.dto;

public record AuthRequestDTO(
    String email,
    String password
) {}
