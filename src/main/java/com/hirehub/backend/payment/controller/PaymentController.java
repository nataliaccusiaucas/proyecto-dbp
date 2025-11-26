package com.hirehub.backend.payment.controller;

import com.hirehub.backend.payment.dto.CreateChargeRequest;
import com.hirehub.backend.payment.dto.CreateChargeResponse;
import com.hirehub.backend.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/charge")
    public ResponseEntity<CreateChargeResponse> createCharge(
            @RequestBody CreateChargeRequest req
    ) {
        return ResponseEntity.ok(paymentService.createCharge(req));
    }
}
