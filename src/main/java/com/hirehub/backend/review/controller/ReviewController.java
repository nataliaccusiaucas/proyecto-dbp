package com.hirehub.backend.review.controller;

import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.review.dto.ReviewRequestDTO;
import com.hirehub.backend.review.dto.ReviewResponseDTO;
import com.hirehub.backend.review.dto.ReviewSummaryDTO;
import com.hirehub.backend.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO dto) {
        Review review = reviewService.createReview(dto);
        return ResponseEntity.ok(mapToResponse(review));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsForFreelancer(@PathVariable UUID freelancerId) {
        var reviews = reviewService.getReviewsForFreelancer(freelancerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(reviews);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/freelancer/{freelancerId}/summary")
    public ResponseEntity<ReviewSummaryDTO> getFreelancerReviewSummary(@PathVariable UUID freelancerId) {
        var reviews = reviewService.getReviewsForFreelancer(freelancerId);
        double avg = reviewService.calculateAverageRating(freelancerId);
        var dtoList = reviews.stream().map(this::mapToResponse).toList();
        return ResponseEntity.ok(new ReviewSummaryDTO(avg, dtoList.size(), dtoList));
    }

    private ReviewResponseDTO mapToResponse(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getAuthor().getName(),
                review.getTarget().getName(),
                review.getJobRequest().getTitle(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}