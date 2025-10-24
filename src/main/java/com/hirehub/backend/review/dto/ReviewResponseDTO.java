package com.hirehub.backend.review.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDTO(
        UUID id,
        String authorName,
        String targetName,
        String jobTitle,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {}