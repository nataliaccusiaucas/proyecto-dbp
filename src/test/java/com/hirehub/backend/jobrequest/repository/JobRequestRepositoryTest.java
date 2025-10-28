package com.hirehub.backend.jobrequest.repository;

import com.hirehub.backend.common.BaseIntegrationTest;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class JobRequestRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User client;

    @BeforeEach
    void setUp() {
        client = new User("Client", "client@test.com", null, Role.CLIENT, "pass");
        userRepository.save(client);
    }

    @Test
    @DisplayName("Guarda correctamente una solicitud de trabajo en PostgreSQL (Testcontainers)")
    void testSaveJobRequest() {
        JobRequest job = new JobRequest(
                "Logo Design",
                "Create a professional logo",
                200.0,
                JobStatus.OPEN,
                client
        );
        JobRequest saved = jobRequestRepository.save(job);

        assertNotNull(saved.getId());
        assertEquals("Logo Design", saved.getTitle());
        assertEquals(JobStatus.OPEN, saved.getStatus());
        assertEquals(client.getEmail(), saved.getClient().getEmail());
    }

    @Test
    @DisplayName("Recupera todas las solicitudes de trabajo guardadas")
    void testFindAllJobRequests() {
        JobRequest job1 = new JobRequest("Mobile App", "Build an Android app", 1000.0, JobStatus.OPEN, client);
        JobRequest job2 = new JobRequest("Website", "Create an e-commerce site", 1500.0, JobStatus.OPEN, client);

        jobRequestRepository.saveAll(List.of(job1, job2));

        List<JobRequest> jobs = jobRequestRepository.findAll();
        assertEquals(2, jobs.size());
        assertTrue(jobs.stream().anyMatch(j -> j.getTitle().equals("Website")));
    }

    @Test
    @DisplayName("Filtra correctamente las solicitudes por cliente")
    void testFindByClient() {
        JobRequest job = new JobRequest("API Development", "Develop REST APIs", 800.0, JobStatus.OPEN, client);
        jobRequestRepository.save(job);

        List<JobRequest> jobs = jobRequestRepository.findByClient(client);
        assertEquals(1, jobs.size());
        assertEquals(client.getEmail(), jobs.getFirst().getClient().getEmail());
    }
}
