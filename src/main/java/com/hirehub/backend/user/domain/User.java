package com.hirehub.backend.user.domain;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String password; 

    @Column(name = "completed_jobs")
    private Integer completedJobs = 0;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String email, String phone, Role role, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role != null ? role : Role.CLIENT;
        this.createdAt = LocalDateTime.now();
        this.averageRating = 0.0;
        this.completedJobs = 0;
        this.password = password;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Integer getCompletedJobs() { return completedJobs; }
    public void setCompletedJobs(Integer completedJobs) { this.completedJobs = completedJobs; }

    public void incrementCompletedJobs() {
        if (this.completedJobs == null) this.completedJobs = 0;
        this.completedJobs++;
    }

    public void setPassword(String password) { this.password = password; }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; 
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
