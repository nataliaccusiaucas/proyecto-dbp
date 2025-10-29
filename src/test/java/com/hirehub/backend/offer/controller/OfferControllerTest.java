package com.hirehub.backend.offer.controller;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.service.OfferService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OfferController.class)
@AutoConfigureMockMvc(addFilters = false)
class OfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OfferService offerService;

    @TestConfiguration
    static class MockBeans {
        @Bean OfferService offerService() { return Mockito.mock(OfferService.class); }
        @Bean JwtService jwtService() { return Mockito.mock(JwtService.class); }
        @Bean CustomUserDetailsService userDetailsService() { return Mockito.mock(CustomUserDetailsService.class); }
    }

    private Offer offer(String freelancerName, String jobTitle) {
        User freelancer = new User();
        freelancer.setId(UUID.randomUUID());
        freelancer.setName(freelancerName);

        JobRequest job = new JobRequest();
        job.setId(UUID.randomUUID());
        job.setTitle(jobTitle);

        Offer o = new Offer();
        o.setId(UUID.randomUUID());
        o.setProposedBudget(500.0);
        o.setProposalText("Propuesta");
        o.setStatus(OfferStatus.PENDING);
        o.setJobRequest(job);
        o.setFreelancer(freelancer);
        o.setCreatedAt(LocalDateTime.now());
        return o;
    }

    @Test
    @DisplayName("GET /api/offers devuelve lista de ofertas")
    void getAllOffers_returnsList() throws Exception {
        when(offerService.getAllOffers()).thenReturn(List.of(
                offer("Ana", "Diseño logo"),
                offer("Beto", "Landing Page")
        ));

        mockMvc.perform(get("/api/offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].freelancerName").value("Ana"))
                .andExpect(jsonPath("$[1].jobRequestTitle").value("Landing Page"));

    }
}