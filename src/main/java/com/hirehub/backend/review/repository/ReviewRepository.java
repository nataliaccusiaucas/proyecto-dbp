package com.hirehub.backend.review.repository;

import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByTarget(User target);

    List<Review> findByAuthor(User author);

    List<Review> findByJobRequest(JobRequest jobRequest);
}
