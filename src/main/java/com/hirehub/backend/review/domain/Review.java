package com.hirehub.backend.review.domain;

import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // quién escribió la reseña (cliente o freelancer)

    @ManyToOne
    @JoinColumn(name = "target_id", nullable = false)
    private User target; // a quién va dirigida (el otro usuario)

    @ManyToOne
    @JoinColumn(name = "job_request_id", nullable = false)
    private JobRequest jobRequest;

    @Column(nullable = false)
    private Integer rating; // de 1 a 5

    @Column(length = 1000)
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Review() {}

    public Review(User author, User target, JobRequest jobRequest, Integer rating, String comment) {
        this.author = author;
        this.target = target;
        this.jobRequest = jobRequest;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters y Setters
    public UUID getId() { return id; }
    public User getAuthor() { return author; }
    public User getTarget() { return target; }
    public JobRequest getJobRequest() { return jobRequest; }
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
