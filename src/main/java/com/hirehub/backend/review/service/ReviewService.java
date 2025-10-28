package com.hirehub.backend.review.service;

import com.hirehub.backend.review.domain.Review;
import com.hirehub.backend.review.dto.ReviewRequestDTO;
import com.hirehub.backend.review.repository.ReviewRepository;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final JobRequestRepository jobRequestRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, JobRequestRepository jobRequestRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.jobRequestRepository = jobRequestRepository;
    }

    public Review createReview(ReviewRequestDTO dto) {
        var author = userRepository.findById(dto.authorId())
                .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
        var target = userRepository.findById(dto.targetId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario objetivo no encontrado"));
        var jobRequest = jobRequestRepository.findById(dto.jobRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado"));

        Review review = new Review(author, target, jobRequest, dto.rating(), dto.comment());
        reviewRepository.save(review);

        var reviews = reviewRepository.findByTarget(target);
        double newAverage = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        target.setAverageRating(newAverage);
        userRepository.save(target);

        return review;
    }

    public List<Review> getReviewsForFreelancer(UUID freelancerId) {
        var freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado"));
        return reviewRepository.findByTarget(freelancer);
    }

    public double calculateAverageRating(UUID freelancerId) {
        var freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado"));
        var reviews = reviewRepository.findByTarget(freelancer);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}