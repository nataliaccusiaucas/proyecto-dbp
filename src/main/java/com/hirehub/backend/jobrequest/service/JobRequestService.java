package com.hirehub.backend.jobrequest.service;

import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository; 

    public JobRequestService(JobRequestRepository jobRequestRepository,
                             UserRepository userRepository,
                             OfferRepository offerRepository) {
        this.jobRequestRepository = jobRequestRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
    }

    public JobRequest createJobRequest(String title, String description, Double budget, UUID clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));

        JobRequest jobRequest = new JobRequest(
                title,
                description,
                budget,
                JobStatus.OPEN,
                client
        );

        return jobRequestRepository.save(jobRequest);
    }

    public JobRequest markAsCompleted(UUID jobRequestId) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado con ID: " + jobRequestId));

        if (jobRequest.getStatus() != JobStatus.IN_PROGRESS) {
            throw new IllegalStateException("Solo los trabajos en progreso pueden marcarse como completados.");
        }

        jobRequest.setStatus(JobStatus.COMPLETED);
        jobRequestRepository.save(jobRequest);

        Offer acceptedOffer = offerRepository.findByJobRequest(jobRequest)
                .stream()
                .filter(o -> o.getStatus() == OfferStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No hay oferta aceptada asociada a este trabajo."));

        User freelancer = acceptedOffer.getFreelancer();

        freelancer.incrementCompletedJobs();

        userRepository.save(freelancer);

        return jobRequest;
    }

    public List<JobRequest> getAllJobRequests() {
        return jobRequestRepository.findAll();
    }

    public List<JobRequest> getJobRequestsByClient(UUID clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));
        return jobRequestRepository.findByClient(client);
    }

    public JobRequest getJobRequestById(UUID id) {
        return jobRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada con ID: " + id));
    }
}
