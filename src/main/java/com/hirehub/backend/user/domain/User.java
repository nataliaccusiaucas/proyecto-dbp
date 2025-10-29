package com.hirehub.backend.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    private String phone;
    @Getter
    private String avatarUrl;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Getter
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String password; 

    @Getter
    @Column(name = "completed_jobs")
    private Integer completedJobs = 0;

    @Getter
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

    public void incrementCompletedJobs() {
        if (this.completedJobs == null) this.completedJobs = 0;
        this.completedJobs++;
    }


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
