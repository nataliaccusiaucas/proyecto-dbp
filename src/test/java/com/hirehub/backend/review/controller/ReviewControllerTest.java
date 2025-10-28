package com.hirehub.backend.review.controller;

import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService)).build();
    }

    @Test
    @DisplayName("🔍 Devuelve reseñas de un freelancer (GET /api/reviews/freelancer/{id})")
    void testGetReviewsForFreelancer() throws Exception {
        UUID freelancerId = UUID.randomUUID();

        Review review1 = Mockito.mock(Review.class);
        Review review2 = Mockito.mock(Review.class);

        Mockito.when(reviewService.getReviewsForFreelancer(any(UUID.class)))
                .thenReturn(List.of(review1, review2));

        mockMvc.perform(get("/api/reviews/freelancer/" + freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Calcula el promedio de reseñas de un freelancer (GET /api/reviews/average/{id})")
    void testCalculateAverageRating() throws Exception {
        UUID freelancerId = UUID.randomUUID();
        Mockito.when(reviewService.calculateAverageRating(freelancerId))
                .thenReturn(4.5);

        mockMvc.perform(get("/api/reviews/average/" + freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("4.5"));
    }
}