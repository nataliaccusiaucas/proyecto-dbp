package com.hirehub.backend.commission.controller;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.commission.service.CommissionService;
import com.hirehub.backend.commission.dto.CommissionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("permitAll()")
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

    @PreAuthorize("permitAll()")
    @PatchMapping("/{commissionId}/pay")
    public ResponseEntity<Commission> markAsPaid(@PathVariable UUID commissionId) {
        return ResponseEntity.ok(commissionService.markAsPaid(commissionId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CommissionResponseDTO>> getAllCommissions() {
        List<Commission> commissions = commissionService.getAllCommissions();

        var response = commissions.stream()
                .map(c -> new CommissionResponseDTO(
                        c.getId(),
                        c.getJobRequest().getTitle(),
                        c.getAmount(),
                        c.getStatus().name(),
                        c.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

}






    

