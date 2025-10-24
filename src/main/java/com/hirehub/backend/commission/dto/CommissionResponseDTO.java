package com.hirehub.backend.commission.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommissionResponseDTO {

    private UUID id;
    private String jobTitle;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;

    public CommissionResponseDTO(UUID id, String jobTitle, Double amount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}