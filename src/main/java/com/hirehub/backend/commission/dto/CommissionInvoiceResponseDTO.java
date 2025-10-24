package com.hirehub.backend.commission.dto;

import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.commission.domain.InvoiceStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public class CommissionInvoiceResponseDTO {
    private UUID id;
    private String freelancerName;
    private Double amount;
    private LocalDateTime issuedAt;
    private LocalDateTime dueDate;
    private InvoiceStatus status;

    public CommissionInvoiceResponseDTO(CommissionInvoice invoice) {
        this.id = invoice.getId();
        this.freelancerName = invoice.getFreelancer().getName();
        this.amount = invoice.getAmount();
        this.issuedAt = invoice.getIssuedAt();
        this.dueDate = invoice.getDueDate();
        this.status = invoice.getStatus();
    }

    public UUID getId() { return id; }
    public String getFreelancerName() { return freelancerName; }
    public Double getAmount() { return amount; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getDueDate() { return dueDate; }
    public InvoiceStatus getStatus() { return status; }
}
