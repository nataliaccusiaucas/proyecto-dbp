package com.hirehub.backend.offer.dto;

import com.hirehub.backend.offer.domain.OfferStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record OfferResponseDTO(
        UUID id,
        Double proposedBudget,
        String proposalText,
        OfferStatus status,
        UUID jobRequestId,
        String jobRequestTitle,
        UUID freelancerId,
        String freelancerName,
        LocalDateTime createdAt,
        
        String freelancerTitle,
        String freelancerDescription,
        String freelancerSkills,
        String freelancerPortfolioUrl,
        String freelancerLocation
) {}
