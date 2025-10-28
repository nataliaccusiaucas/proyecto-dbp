package com.hirehub.backend.offer.controller;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.service.OfferService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class OfferControllerTest {

    @Mock
    private OfferService offerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OfferController(offerService)).build();
    }

    @Test
    @DisplayName("Devuelve todas las ofertas existentes correctamente (GET /offers)")
    void testGetAllOffers() throws Exception {
        Offer offer1 = new Offer(1000.0, "Diseño de app", OfferStatus.PENDING, null, null);
        Offer offer2 = new Offer(1500.0, "Desarrollo web", OfferStatus.ACCEPTED, null, null);

        when(offerService.getAllOffers()).thenReturn(List.of(offer1, offer2));

        mockMvc.perform(get("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].proposedBudget").value(1000.0))
                .andExpect(jsonPath("$[1].status").value("ACCEPTED"));
    }

    @Test
    @DisplayName("Devuelve ofertas por freelancer (GET /offers/freelancer/{id})")
    void testGetOffersByFreelancer() throws Exception {
        UUID freelancerId = UUID.randomUUID();

        Offer offer = new Offer(800.0, "Landing Page", OfferStatus.PENDING, null, null);
        when(offerService.getOffersByFreelancer(any(UUID.class)))
                .thenReturn(List.of(offer));

        mockMvc.perform(get("/api/offers/freelancer/" + freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].proposalText").value("Landing Page"));
    }

    @Test
    @DisplayName("Devuelve ofertas por JobRequest (GET /offers/job/{id})")
    void testGetOffersByJobRequest() throws Exception {
        UUID jobId = UUID.randomUUID();

        Offer offer = new Offer(500.0, "Maquetación Figma", OfferStatus.PENDING, null, null);
        when(offerService.getOffersByJobRequest(any(UUID.class)))
                .thenReturn(List.of(offer));

        mockMvc.perform(get("/api/offers/job/" + jobId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].proposalText").value("Maquetación Figma"))
                .andExpect(jsonPath("$[0].proposedBudget").value(500.0));
    }
}
