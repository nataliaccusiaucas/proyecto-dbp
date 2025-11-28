package com.hirehub.backend.jobrequest.domain;

import com.hirehub.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "job_requests")
@Getter
@Setter
public class JobRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "job_request_categories",
            joinColumns = @JoinColumn(name = "job_request_id"))
    @Column(name = "category")
    private List<String> categories;

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

    public JobRequest(
            String title,
            List<String> categories,
            String description,
            Double budget,
            JobStatus status,
            User client
    ) {
        this.title = title;
        this.categories = categories;
        this.description = description;
        this.budget = budget;
        this.status = status;
        this.client = client;
        this.createdAt = LocalDateTime.now();
    }
}
