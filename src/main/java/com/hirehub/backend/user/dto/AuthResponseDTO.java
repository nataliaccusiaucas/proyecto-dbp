package com.hirehub.backend.user.dto;

public record AuthResponseDTO(
        String message,
        String token
) {}