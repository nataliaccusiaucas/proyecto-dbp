package com.hirehub.backend.freelancerprofile.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "freelancer_profiles")
public class FreelancerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private com.hirehub.backend.user.domain.User user;

    @Setter
    private String title;
    @Setter
    private String description;
    @Setter
    private String skills;
    @Setter
    @Column(name = "portfolio_url")
    private String portfolioUrl;
    @Setter
    private String location;

    public FreelancerProfile() {}
}
