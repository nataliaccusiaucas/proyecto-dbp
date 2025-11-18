package com.hirehub.backend.offer.service;

import com.hirehub.backend.common.exception.ResourceNotFoundException;
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

    public OfferService(OfferRepository offerRepository,
                        JobRequestRepository jobRequestRepository,
                        UserRepository userRepository,
                        CommissionService commissionService) {
        this.offerRepository = offerRepository;
        this.jobRequestRepository = jobRequestRepository;
        this.userRepository = userRepository;
        this.commissionService = commissionService;
    }

    public Offer createOffer(Double proposedBudget, String proposalText, UUID jobRequestId, UUID freelancerId) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de trabajo no encontrada con ID: " + jobRequestId));

        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado con ID: " + freelancerId));

        Offer offer = new Offer(proposedBudget, proposalText, OfferStatus.PENDING, jobRequest, freelancer);
        return offerRepository.save(offer);
    }

    public Offer updateOfferStatus(UUID offerId, OfferStatus status) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con ID: " + offerId));

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

    public List<Offer> getOffersForClient(UUID clientId) {

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));

        // Obtener TODOS los jobRequests del cliente
        List<JobRequest> clientRequests = jobRequestRepository.findByClient(client);

        // Juntar TODAS las ofertas recibidas en todos sus job requests
        return clientRequests.stream()
                .flatMap(req -> offerRepository.findByJobRequest(req).stream())
                .toList();
    }


    public List<Offer> getOffersByJobRequest(UUID jobRequestId) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con ID: " + jobRequestId));
        return offerRepository.findByJobRequest(jobRequest);
    }

    public List<Offer> getOffersByFreelancer(UUID freelancerId) {
        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer no encontrado con ID: " + freelancerId));
        return offerRepository.findByFreelancer(freelancer);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }
}