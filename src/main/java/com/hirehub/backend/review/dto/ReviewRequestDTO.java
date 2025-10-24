package com.hirehub.backend.review.dto;

import jakarta.validation.constraints.*;

import java.util.UUID;

public record ReviewRequestDTO(
        @NotNull UUID authorId,
        @NotNull UUID targetId,
        @NotNull UUID jobRequestId,
        @Min(1) @Max(5) Integer rating,
        @NotBlank String comment
) {}