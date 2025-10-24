package com.hirehub.backend.commission.controller;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.commission.service.CommissionService;
import com.hirehub.backend.commission.dto.CommissionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/commissions")
@CrossOrigin(origins = "*")
public class CommissionController {

    private final CommissionService commissionService;

    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    @GetMapping("/freelancer/{freelancerId}")
    public List<CommissionResponseDTO> getCommissionsByFreelancer(@PathVariable UUID freelancerId) {
        List<Commission> commissions = commissionService.getCommissionsByFreelancer(freelancerId);

        return commissions.stream()
                .map(c -> new CommissionResponseDTO(
                        c.getId(),
                        c.getJobRequest().getTitle(),
                        c.getAmount(),
                        c.getStatus().name(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @PatchMapping("/{commissionId}/pay")
    public ResponseEntity<Commission> markAsPaid(@PathVariable UUID commissionId) {
        return ResponseEntity.ok(commissionService.markAsPaid(commissionId));
    }
}






    

