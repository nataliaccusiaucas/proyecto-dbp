package com.hirehub.backend.commission.domain;

import com.hirehub.backend.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "commissions_invoices")
public class CommissionInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    protected CommissionInvoice() {}

    public CommissionInvoice(User freelancer, double amount) {
        this.freelancer = freelancer;
        this.amount = amount;
        this.issuedAt = LocalDateTime.now();
        this.status = InvoiceStatus.PENDING;
    }

    public UUID getId() {
        return id;
    }

    public User getFreelancer() {
        return freelancer;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}