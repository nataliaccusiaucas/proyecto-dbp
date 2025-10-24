package com.hirehub.backend.freelancerprofile.dto;

public record FreelancerProfileUpdateDTO(
        String title,
        String description,
        String skills,
        String portfolioUrl,
        String location
) {}
