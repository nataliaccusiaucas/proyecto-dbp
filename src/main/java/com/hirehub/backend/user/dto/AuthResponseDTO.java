package com.hirehub.backend.user.dto;

public record AuthResponseDTO(
        UserResponseDTO user,
        String token
) {}