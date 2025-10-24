package com.hirehub.backend.offer.domain;

import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Double proposedBudget;

    @Column(nullable = false, length = 2000)
    private String proposalText;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @ManyToOne
    @JoinColumn(name = "job_request_id")
    private JobRequest jobRequest;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    private LocalDateTime createdAt;

    public Offer() {}

    public Offer(Double proposedBudget, String proposalText, OfferStatus status, JobRequest jobRequest, User freelancer) {
        this.proposedBudget = proposedBudget;
        this.proposalText = proposalText;
        this.status = status;
        this.jobRequest = jobRequest;
        this.freelancer = freelancer;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public Double getProposedBudget() { return proposedBudget; }
    public String getProposalText() { return proposalText; }
    public OfferStatus getStatus() { return status; }
    public JobRequest getJobRequest() { return jobRequest; }
    public User getFreelancer() { return freelancer; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setProposedBudget(Double proposedBudget) { this.proposedBudget = proposedBudget; }
    public void setProposalText(String proposalText) { this.proposalText = proposalText; }
    public void setStatus(OfferStatus status) { this.status = status; }
    public void setJobRequest(JobRequest jobRequest) { this.jobRequest = jobRequest; }
    public void setFreelancer(User freelancer) { this.freelancer = freelancer; }
}
