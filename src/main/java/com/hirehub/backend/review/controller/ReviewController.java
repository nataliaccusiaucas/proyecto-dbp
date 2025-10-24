package com.hirehub.backend.review.controller;

import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.review.dto.ReviewRequestDTO;
import com.hirehub.backend.review.dto.ReviewResponseDTO;
import com.hirehub.backend.review.dto.ReviewSummaryDTO;
import com.hirehub.backend.review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponseDTO createReview(@Valid @RequestBody ReviewRequestDTO dto) {
        Review review = reviewService.createReview(dto);
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

    @GetMapping("/freelancer/{freelancerId}")
    public List<ReviewResponseDTO> getReviewsForFreelancer(@PathVariable UUID freelancerId) {
    return reviewService.getReviewsForFreelancer(freelancerId)
            .stream()
            .map(r -> new ReviewResponseDTO(
                    r.getId(),
                    r.getAuthor().getName(),
                    r.getTarget().getName(),
                    r.getJobRequest().getTitle(),
                    r.getRating(),
                    r.getComment(),
                    r.getCreatedAt()
            ))
            .collect(Collectors.toList());
}

    @GetMapping("/freelancer/{freelancerId}/summary")
    public ReviewSummaryDTO getFreelancerReviewSummary(@PathVariable UUID freelancerId) {
    var reviews = reviewService.getReviewsForFreelancer(freelancerId);

    double averageRating = reviewService.calculateAverageRating(freelancerId);

    var reviewDTOs = reviews.stream()
            .map(r -> new ReviewResponseDTO(
                    r.getId(),
                    r.getAuthor().getName(),
                    r.getTarget().getName(),
                    r.getJobRequest().getTitle(),
                    r.getRating(),
                    r.getComment(),
                    r.getCreatedAt()
            ))
            .toList();

    return new ReviewSummaryDTO(averageRating, reviewDTOs.size(), reviewDTOs);
}

}