package com.hirehub.backend.chat.dto;

public record SendMessageRequestDTO(
        String content,
        String senderId
) {}
