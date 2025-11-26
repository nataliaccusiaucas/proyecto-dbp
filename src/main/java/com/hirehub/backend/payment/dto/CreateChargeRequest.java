package com.hirehub.backend.payment.dto;

import lombok.Data;

@Data
public class CreateChargeRequest {
    private String email;
    private Integer amount;
    private String sourceId;
}
