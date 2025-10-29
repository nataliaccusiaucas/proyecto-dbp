package com.hirehub.backend.jobrequest.domain;

import com.hirehub.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "job_requests")
public class JobRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false, length = 2000)
    private String description;

    @Setter
    private Double budget;

    @Setter
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Setter
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

    public void setId(UUID uuid) {
    }

    public void setCreatedAt(LocalDateTime now) {
    }
}
