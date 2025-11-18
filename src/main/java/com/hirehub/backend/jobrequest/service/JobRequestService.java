package com.hirehub.backend.jobrequest.service;

import com.hirehub.backend.common.exception.InvalidOperationException;
import com.hirehub.backend.common.exception.ResourceNotFoundException;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.dto.JobRequestRequestDTO;
import com.hirehub.backend.jobrequest.event.JobRequestCreatedEvent;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.repository.OfferRepository;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public JobRequestService(JobRequestRepository jobRequestRepository,
            UserRepository userRepository,
            OfferRepository offerRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.jobRequestRepository = jobRequestRepository;
        this.userRepository = userRepository;
        this.offerRepository = offerRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public JobRequest createJobRequest(JobRequestRequestDTO dto) {

        User client = userRepository.findById(dto.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + dto.clientId()));

        JobRequest jobRequest = new JobRequest(
                dto.title(),
                dto.category(),
                dto.description(),
                dto.budget(),
                JobStatus.OPEN,
                client
        );

        jobRequestRepository.save(jobRequest);

        applicationEventPublisher.publishEvent(new JobRequestCreatedEvent(this, jobRequest));

        return jobRequest;
    }

    public JobRequest markAsCompleted(UUID jobRequestId) {
        JobRequest jobRequest = jobRequestRepository.findById(jobRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado con ID: " + jobRequestId));

        if (jobRequest.getStatus() != JobStatus.IN_PROGRESS) {
            throw new InvalidOperationException("Solo los trabajos en progreso pueden marcarse como completados.");
        }

        jobRequest.setStatus(JobStatus.COMPLETED);
        jobRequestRepository.save(jobRequest);



        Offer acceptedOffer = offerRepository.findByJobRequest(jobRequest)
                .stream()
                .filter(o -> o.getStatus() == OfferStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No hay oferta aceptada asociada a este trabajo."));

        User freelancer = acceptedOffer.getFreelancer();
        freelancer.incrementCompletedJobs();
        userRepository.save(freelancer);

        return jobRequest;
    }

    public List<JobRequest> getJobRequestsByClient(UUID clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));
        return jobRequestRepository.findByClient(client);
    }

    public JobRequest getJobRequestById(UUID id) {
        return jobRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con ID: " + id));
    }

    public List<JobRequest> getAllJobRequests() {
        return jobRequestRepository.findAll();
    }
}