package com.hirehub.backend.offer.service;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.repository.OfferRepository;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.commission.service.CommissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final JobRequestRepository jobRequestRepository;
    private final UserRepository userRepository;
    private final CommissionService commissionService;

    
    public OfferService(
            OfferRepository offerRepository,
            JobRequestRepository jobRequestRepository,
            UserRepository userRepository,
            CommissionService commissionService
    ) {
        this.offerRepository = offerRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.userRepository = userRepository;
        this.commissionService = commissionService;
    }

    public Offer createOffer(Double proposedBudget, String proposalText, UUID jobRequestId, UUID freelancerId) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud de trabajo no encontrada con ID: " + jobRequestId));

        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado con ID: " + freelancerId));

        Offer offer = new Offer(proposedBudget, proposalText, OfferStatus.PENDING, jobRequest, freelancer);
        return offerRepository.save(offer);
    }

    public List<Offer> getOffersByJobRequest(UUID jobRequestId) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + jobRequestId));
        return offerRepository.findByJobRequest(jobRequest);
    }

    public List<Offer> getOffersByFreelancer(UUID freelancerId) {
        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado con ID: " + freelancerId));
        return offerRepository.findByFreelancer(freelancer);
    }

    public Offer updateOfferStatus(UUID offerId, OfferStatus status) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada con ID: " + offerId));

        offer.setStatus(status);
        offerRepository.save(offer);

        if (status == OfferStatus.ACCEPTED) {
            var jobRequest = offer.getJobRequest();
            jobRequest.setStatus(JobStatus.IN_PROGRESS);
            jobRequestRepository.save(jobRequest);

            commissionService.createCommission(
                    offer.getFreelancer(),
                    offer.getJobRequest(),
                    offer.getProposedBudget()
            );
        }

        return offer;
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }
}
