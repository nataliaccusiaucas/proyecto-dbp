package com.hirehub.backend.review.service;

import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.review.dto.ReviewRequestDTO;
import com.hirehub.backend.review.repository.ReviewRepository;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
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

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JobRequestRepository jobRequestRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User author;
    private User target;
    private JobRequest jobRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new User("Client", "client@test.com", null, Role.CLIENT, "pass");
        target = new User("Freelancer", "free@test.com", null, Role.FREELANCER, "pass");
        jobRequest = new JobRequest("Title", "Description", 100.0, JobStatus.OPEN, author);
    }

    @Test
    void testCreateReview_Success() {
        UUID authorId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();
        UUID jobId = UUID.randomUUID();

        ReviewRequestDTO dto = new ReviewRequestDTO(authorId, targetId, jobId, 5, "Excellent");

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(userRepository.findById(targetId)).thenReturn(Optional.of(target));
        when(jobRequestRepository.findById(jobId)).thenReturn(Optional.of(jobRequest));
        when(reviewRepository.findByTarget(target))
                .thenReturn(List.of(new Review(author, target, jobRequest, 5, "Excellent")));

        Review saved = new Review(author, target, jobRequest, 5, "Excellent");
        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        Review created = reviewService.createReview(dto);

        assertNotNull(created);
        assertEquals(5, created.getRating());
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetReviewsForFreelancer() {
        UUID freelancerId = UUID.randomUUID();
        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(target));
        when(reviewRepository.findByTarget(target))
                .thenReturn(List.of(new Review(author, target, jobRequest, 5, "Good work")));

        List<Review> reviews = reviewService.getReviewsForFreelancer(freelancerId);
        assertEquals(1, reviews.size());
    }

    @Test
    void testCalculateAverageRating() {
        UUID freelancerId = UUID.randomUUID();
        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(target));
        when(reviewRepository.findByTarget(target)).thenReturn(List.of(
                new Review(author, target, jobRequest, 4, "Good"),
                new Review(author, target, jobRequest, 5, "Excellent")
        ));

        double avg = reviewService.calculateAverageRating(freelancerId);
        assertEquals(4.5, avg, 0.01);
    }
}