package com.hirehub.backend.payment.dto;

public class CreateChargeResponse {
    public boolean success;
    public String chargeId;
    public String message;

    public CreateChargeResponse(boolean success, String chargeId, String message) {
        this.success = success;
        this.chargeId = chargeId;
        this.message = message;
    }
}
