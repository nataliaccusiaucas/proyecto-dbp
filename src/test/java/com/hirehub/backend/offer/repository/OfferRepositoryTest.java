package com.hirehub.backend.offer.repository;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OfferRepositoryTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User client;
    private User freelancer;
    private JobRequest jobRequest;

    @BeforeEach
    void setUp() {
        client = new User("Client A", "client@test.com", null, Role.CLIENT, "pass");
        freelancer = new User("Freelancer B", "free@test.com", null, Role.FREELANCER, "pass");

        userRepository.save(client);
        userRepository.save(freelancer);

        jobRequest = new JobRequest("Website", "Build a responsive website", 500.0, JobStatus.OPEN, client);
        jobRequestRepository.save(jobRequest);
    }

    @Test
    void testFindByJobRequest() {
        Offer offer1 = new Offer(450.0, "I can do it fast!", OfferStatus.PENDING, jobRequest, freelancer);
        offerRepository.save(offer1);

        List<Offer> offers = offerRepository.findByJobRequest(jobRequest);
        assertEquals(1, offers.size());
        assertEquals(OfferStatus.PENDING, offers.get(0).getStatus());
    }

    @Test
    void testFindByFreelancer() {
        Offer offer = new Offer(400.0, "Let's collaborate!", OfferStatus.PENDING, jobRequest, freelancer);
        offerRepository.save(offer);

        List<Offer> offers = offerRepository.findByFreelancer(freelancer);
        assertFalse(offers.isEmpty());
        assertEquals(freelancer.getEmail(), offers.get(0).getFreelancer().getEmail());
    }

    @Test
    void testSaveAndRetrieveOffer() {
        Offer offer = new Offer(600.0, "Best quality guaranteed!", OfferStatus.PENDING, jobRequest, freelancer);
        Offer saved = offerRepository.save(offer);

        assertNotNull(saved.getId());
        assertEquals(600.0, saved.getProposedBudget());
        assertEquals("Best quality guaranteed!", saved.getProposalText());
    }
}