package com.hirehub.backend.payment.dto;

public class CreateChargeResponseDTO {
    public boolean success;
    public String chargeId;
    public String message;

    public CreateChargeResponseDTO (boolean success, String chargeId, String message) {
        this.success = success;
        this.chargeId = chargeId;
        this.message = message;
    }
}
