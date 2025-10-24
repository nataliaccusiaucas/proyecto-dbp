package com.hirehub.backend.jobrequest.repository;

import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobRequestRepository extends JpaRepository<JobRequest, UUID> {

    List<JobRequest> findByClient(User client);
    
}
