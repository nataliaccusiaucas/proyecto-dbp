package com.hirehub.backend.chat.domain;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "job_request_id")
    private JobRequest jobRequest;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Conversation() {
        this.createdAt = LocalDateTime.now();
    }

    public Conversation(User client, User freelancer, JobRequest jobRequest) {
        this.client = client;
        this.freelancer = freelancer;
        this.jobRequest = jobRequest;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public User getClient() { return client; }
    public User getFreelancer() { return freelancer; }
    public JobRequest getJobRequest() { return jobRequest; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
