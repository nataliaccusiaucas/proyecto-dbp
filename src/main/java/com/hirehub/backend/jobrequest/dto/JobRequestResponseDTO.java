package com.hirehub.backend.jobrequest.dto;

import com.hirehub.backend.jobrequest.domain.JobStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record JobRequestResponseDTO(
        UUID id,
        String title,
        String description,
        Double budget,
        JobStatus status,
        UUID clientId,
        String clientName,
        LocalDateTime createdAt
) {}
