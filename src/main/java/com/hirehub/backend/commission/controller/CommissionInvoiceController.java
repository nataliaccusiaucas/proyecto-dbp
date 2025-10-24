package com.hirehub.backend.commission.controller;

import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.commission.dto.CommissionInvoiceResponseDTO;
import com.hirehub.backend.commission.service.CommissionInvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commission-invoices")
@CrossOrigin(origins = "*")
public class CommissionInvoiceController {

    private final CommissionInvoiceService commissionInvoiceService;

    public CommissionInvoiceController(CommissionInvoiceService commissionInvoiceService) {
        this.commissionInvoiceService = commissionInvoiceService;
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<CommissionInvoiceResponseDTO>> getInvoicesByFreelancer(@PathVariable UUID freelancerId) {
        List<CommissionInvoiceResponseDTO> response = commissionInvoiceService.getInvoicesByFreelancer(freelancerId)
                .stream()
                .map(CommissionInvoiceResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{invoiceId}/pay")
    public ResponseEntity<CommissionInvoiceResponseDTO> markAsPaid(@PathVariable UUID invoiceId) {
        CommissionInvoice invoice = commissionInvoiceService.markAsPaid(invoiceId);
        return ResponseEntity.ok(new CommissionInvoiceResponseDTO(invoice));
    }
}
