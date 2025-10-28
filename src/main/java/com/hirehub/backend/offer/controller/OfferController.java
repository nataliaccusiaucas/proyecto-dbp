package com.hirehub.backend.offer.controller;

import com.hirehub.backend.offer.domain.Offer;
import com.hirehub.backend.offer.domain.OfferStatus;
import com.hirehub.backend.offer.dto.OfferRequestDTO;
import com.hirehub.backend.offer.dto.OfferResponseDTO;
import com.hirehub.backend.offer.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
public class OfferController {


    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public OfferResponseDTO createOffer(@Valid @RequestBody OfferRequestDTO dto) {
        Offer offer = offerService.createOffer(
                dto.proposedBudget(),
                dto.proposalText(),
                dto.jobRequestId(),
                dto.freelancerId()
        );

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

    @GetMapping("/job-request/{jobRequestId}")
    public List<OfferResponseDTO> getOffersByJobRequest(@PathVariable UUID jobRequestId) {
        return offerService.getOffersByJobRequest(jobRequestId)
                .stream()
                .map(offer -> new OfferResponseDTO(
                        offer.getId(),
                        offer.getProposedBudget(),
                        offer.getProposalText(),
                        offer.getStatus(),
                        offer.getJobRequest().getId(),
                        offer.getJobRequest().getTitle(),
                        offer.getFreelancer().getId(),
                        offer.getFreelancer().getName(),
                        offer.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/freelancer/{freelancerId}")
    public List<OfferResponseDTO> getOffersByFreelancer(@PathVariable UUID freelancerId) {
        return offerService.getOffersByFreelancer(freelancerId)
                .stream()
                .map(offer -> new OfferResponseDTO(
                        offer.getId(),
                        offer.getProposedBudget(),
                        offer.getProposalText(),
                        offer.getStatus(),
                        offer.getJobRequest().getId(),
                        offer.getJobRequest().getTitle(),
                        offer.getFreelancer().getId(),
                        offer.getFreelancer().getName(),
                        offer.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @PatchMapping("/{offerId}/status")
    public OfferResponseDTO updateOfferStatus(@PathVariable UUID offerId, @RequestParam OfferStatus status) {
        Offer updated = offerService.updateOfferStatus(offerId, status);

        return new OfferResponseDTO(
                updated.getId(),
                updated.getProposedBudget(),
                updated.getProposalText(),
                updated.getStatus(),
                updated.getJobRequest().getId(),
                updated.getJobRequest().getTitle(),
                updated.getFreelancer().getId(),
                updated.getFreelancer().getName(),
                updated.getCreatedAt()
        );
    }
    @GetMapping
    public List<OfferResponseDTO> getAllOffers() {
        return offerService.getAllOffers()
            .stream()
            .map(offer -> new OfferResponseDTO(
                    offer.getId(),
                    offer.getProposedBudget(),
                    offer.getProposalText(),
                    offer.getStatus(),
                    offer.getJobRequest().getId(),
                    offer.getJobRequest().getTitle(),
                    offer.getFreelancer().getId(),
                    offer.getFreelancer().getName(),
                    offer.getCreatedAt()
            ))
            .collect(Collectors.toList());
}
}