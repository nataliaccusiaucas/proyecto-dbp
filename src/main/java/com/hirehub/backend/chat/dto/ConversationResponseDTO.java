package com.hirehub.backend.chat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationResponseDTO(
        UUID id,
        String jobTitle,
        String clientName,
        String freelancerName,
        LocalDateTime createdAt
) {}
