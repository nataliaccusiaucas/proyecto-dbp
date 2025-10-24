package com.hirehub.backend.freelancerprofile.dto;

import java.util.UUID;

public record FreelancerProfileResponseDTO(
        UUID id,
        String name,
        String title,
        String description,
        String skills,
        String portfolioUrl,
        String location,
        Double averageRating,
        Integer completedJobs
) {}