package com.hirehub.backend.review.dto;

import java.util.List;

public record ReviewSummaryDTO(
        Double averageRating,
        int totalReviews,
        List<ReviewResponseDTO> reviews
) {}