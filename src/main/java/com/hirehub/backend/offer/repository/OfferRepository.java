package com.hirehub.backend.offer.repository;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {

    List<Offer> findByJobRequest(JobRequest jobRequest);

    List<Offer> findByFreelancer(User freelancer);

}

