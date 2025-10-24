package com.hirehub.backend.freelancerprofile.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "freelancer_profiles")
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private com.hirehub.backend.user.domain.User user;

    private String title;
    private String description;
    private String skills;
    @Column(name = "portfolio_url")
    private String portfolioUrl;
    private String location;

    public FreelancerProfile() {}

    public UUID getId() { return id; }

    public com.hirehub.backend.user.domain.User getUser() { return user; }

    public void setUser(com.hirehub.backend.user.domain.User user) { this.user = user; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getSkills() { return skills; }

    public void setSkills(String skills) { this.skills = skills; }

    public String getPortfolioUrl() { return portfolioUrl; }

    public void setPortfolioUrl(String portfolioUrl) { this.portfolioUrl = portfolioUrl; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }
}
