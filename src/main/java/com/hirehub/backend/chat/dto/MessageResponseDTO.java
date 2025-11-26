package com.hirehub.backend.chat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDTO(
        UUID id,
        UUID senderId,
        String senderName,
        String content,
        LocalDateTime createdAt
) {}
