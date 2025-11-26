package com.hirehub.backend.payment.service;

import com.hirehub.backend.payment.config.CulqiConfig;
import com.hirehub.backend.payment.dto.CreateChargeRequest;
import com.hirehub.backend.payment.dto.CreateChargeResponse;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final CulqiConfig culqiConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public PaymentService(CulqiConfig culqiConfig) {
        this.culqiConfig = culqiConfig;
    }

    public CreateChargeResponse createCharge(CreateChargeRequest req) {
        try {

            String url = "https://api.culqi.com/v2/charges";


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(culqiConfig.getSecretKey());


            Map<String, Object> body = new HashMap<>();
            body.put("amount", req.getAmount());
            body.put("currency_code", "PEN");
            body.put("email", req.getEmail());
            body.put("source_id", req.getSourceId());


            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            JSONObject json = new JSONObject(response.getBody());

            return new CreateChargeResponse(
                    true,
                    json.getString("id"),
                    "Pago realizado exitosamente."
            );

        } catch (Exception e) {
            return new CreateChargeResponse(
                    false,
                    null,
                    "Error en Culqi: " + e.getMessage()
            );
        }
    }
}