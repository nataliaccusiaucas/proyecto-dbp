package com.hirehub.backend.offer.controller;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.dto.OfferRequestDTO;
import com.hirehub.backend.offer.dto.OfferResponseDTO;
import com.hirehub.backend.offer.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }
    @PreAuthorize("hasAnyRole('FREELANCER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<OfferResponseDTO> createOffer(@Valid @RequestBody OfferRequestDTO dto) {
        Offer offer = offerService.createOffer(
                dto.proposedBudget(),
                dto.proposalText(),
                dto.jobRequestId(),
                dto.freelancerId()
        );
        return ResponseEntity.ok(mapToResponse(offer));
    }

    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    @PatchMapping("/{offerId}/status")
    public ResponseEntity<OfferResponseDTO> updateOfferStatus(
            @PathVariable UUID offerId,
            @RequestParam OfferStatus status
    ) {
        return ResponseEntity.ok(mapToResponse(offerService.updateOfferStatus(offerId, status)));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'FREELANCER')")
    @GetMapping("/job-request/{jobRequestId}")
    public ResponseEntity<List<OfferResponseDTO>> getOffersByJobRequest(@PathVariable UUID jobRequestId) {
        var offers = offerService.getOffersByJobRequest(jobRequestId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offers);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'FREELANCER')")
    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<OfferResponseDTO>> getOffersByFreelancer(@PathVariable UUID freelancerId) {
        var offers = offerService.getOffersByFreelancer(freelancerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offers);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OfferResponseDTO>> getAllOffers() {
        var offers = offerService.getAllOffers()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(offers);
    }

    private OfferResponseDTO mapToResponse(Offer offer) {
        return new OfferResponseDTO(
                offer.getId(),
                offer.getProposedBudget(),
                offer.getProposalText(),
                offer.getStatus(),
                offer.getJobRequest().getId(),
                offer.getJobRequest().getTitle(),
                offer.getFreelancer().getId(),
                offer.getFreelancer().getName(),
                offer.getCreatedAt()
        );
    }
}