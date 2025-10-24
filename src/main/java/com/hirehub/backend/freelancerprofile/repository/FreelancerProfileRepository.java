package com.hirehub.backend.freelancerprofile.repository;

import com.hirehub.backend.freelancerprofile.domain.FreelancerProfile;
import com.hirehub.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile, UUID> {
    Optional<FreelancerProfile> findByUser(User user);
}
