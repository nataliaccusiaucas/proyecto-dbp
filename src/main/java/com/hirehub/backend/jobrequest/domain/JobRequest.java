package com.hirehub.backend.jobrequest.domain;

import com.hirehub.backend.user.domain.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_requests")
public class JobRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    private Double budget;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    private LocalDateTime createdAt;

    public JobRequest() {}

    public JobRequest(String title, String description, Double budget, JobStatus status, User client) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.status = status;
        this.client = client;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Double getBudget() { return budget; }
    public JobStatus getStatus() { return status; }
    public User getClient() { return client; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setBudget(Double budget) { this.budget = budget; }
    public void setStatus(JobStatus status) { this.status = status; }
    public void setClient(User client) { this.client = client; }
}
