package com.hirehub.backend.commission.domain;

import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "commissions")
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_request_id")
    private JobRequest jobRequest;

    @Column(nullable = false)
    private Double amount; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    protected Commission() {}

    public Commission(User freelancer, JobRequest jobRequest, Double amount) {
        this.freelancer = freelancer;
        this.jobRequest = jobRequest;
        this.amount = amount;
        this.status = CommissionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public User getFreelancer() { return freelancer; }
    public JobRequest getJobRequest() { return jobRequest; }
    public Double getAmount() { return amount; }
    public CommissionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setStatus(CommissionStatus status) { this.status = status; }
}
