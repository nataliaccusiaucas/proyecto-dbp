package com.hirehub.backend.user.dto;

import com.hirehub.backend.user.domain.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phone,
        Role role,
        LocalDateTime createdAt,
        Double averageRating,
        Integer completedJobs
) {}
