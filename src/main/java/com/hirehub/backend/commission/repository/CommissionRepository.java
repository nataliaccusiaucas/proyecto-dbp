package com.hirehub.backend.commission.repository;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CommissionRepository extends JpaRepository<Commission, UUID> {
    List<Commission> findByFreelancer(User freelancer);
}