package com.hirehub.backend.user.dto;

public record AuthResponseDTO(
    String token,
    String message
) {}
