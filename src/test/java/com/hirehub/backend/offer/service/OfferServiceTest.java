package com.hirehub.backend.offer.service;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.repository.OfferRepository;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.commission.service.CommissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private JobRequestRepository jobRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommissionService commissionService;

    @InjectMocks
    private OfferService offerService;

    private User freelancer;
    private User client;
    private JobRequest jobRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new User("Client A", "client@test.com", null, Role.CLIENT, "pass");
        freelancer = new User("Freelancer B", "freelancer@test.com", null, Role.FREELANCER, "pass");
        jobRequest = new JobRequest("Title", "Description", 200.0, JobStatus.OPEN, client);
    }

    @Test
    void testCreateOffer_Success() {
        UUID jobId = UUID.randomUUID();
        UUID freelancerId = UUID.randomUUID();

        when(jobRequestRepository.findById(jobId)).thenReturn(Optional.of(jobRequest));
        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));

        Offer offer = new Offer(250.0, "Proposal text", OfferStatus.PENDING, jobRequest, freelancer);
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer created = offerService.createOffer(250.0, "Proposal text", jobId, freelancerId);

        assertNotNull(created);
        assertEquals(OfferStatus.PENDING, created.getStatus());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void testGetAllOffers() {
        when(offerRepository.findAll()).thenReturn(List.of(
                new Offer(200.0, "Good job", OfferStatus.PENDING, jobRequest, freelancer)
        ));

        List<Offer> offers = offerService.getAllOffers();
        assertEquals(1, offers.size());
        verify(offerRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOfferStatus_AcceptedCreatesCommission() {
        UUID offerId = UUID.randomUUID();
        Offer offer = new Offer(250.0, "Proposal", OfferStatus.PENDING, jobRequest, freelancer);

        when(offerRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer updated = offerService.updateOfferStatus(offerId, OfferStatus.ACCEPTED);

        assertEquals(OfferStatus.ACCEPTED, updated.getStatus());
        assertEquals(JobStatus.IN_PROGRESS, updated.getJobRequest().getStatus());
        verify(commissionService, times(1))
                .createCommission(freelancer, jobRequest, offer.getProposedBudget());
    }
}