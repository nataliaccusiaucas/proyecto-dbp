package com.hirehub.backend.review.controller;

import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.review.dto.ReviewRequestDTO;
import com.hirehub.backend.review.service.ReviewService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;
import com.hirehub.backend.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ReviewService reviewService;

    @TestConfiguration
    static class MockBeans {
        @Bean ReviewService reviewService() { return Mockito.mock(ReviewService.class); }
        @Bean JwtService jwtService() { return Mockito.mock(JwtService.class); }
        @Bean CustomUserDetailsService userDetailsService() { return Mockito.mock(CustomUserDetailsService.class); }
    }

    @Test
    @DisplayName("GET /api/reviews/freelancer/{id} devuelve lista de reviews")
    void getReviewsForFreelancer_ok() throws Exception {
        UUID freelancerId = UUID.randomUUID();
        User author = new User();
        author.setName("Cliente A");

        User target = new User();
        target.setName("Freelancer B");

        Review review = new Review();
        review.setId(UUID.randomUUID());
        review.setAuthor(author);
        review.setTarget(target);
        review.setRating(5);
        review.setComment("Excelente trabajo");

        when(reviewService.getReviewsForFreelancer(freelancerId)).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews/freelancer/{freelancerId}", freelancerId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/reviews crea un nuevo review")
    void createReview_ok() throws Exception {
        ReviewRequestDTO dto = new ReviewRequestDTO(UUID.randomUUID(), UUID.randomUUID(),UUID.randomUUID(), 5, "Buen trabajo");
        when(reviewService.createReview(any())).thenReturn(new Review());

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "authorId": "6b9a9f0a-0000-0000-0000-000000000000",
                            "targetId": "1b9a9f0a-0000-0000-0000-000000000000",
                            "jobRequestId": "5b9a9f0a-0000-0000-0000-000000000000",
                            "rating": 5,
                            "comment": "Buen trabajo"
                        }
                        """))
                .andExpect(status().isOk());
    }
}