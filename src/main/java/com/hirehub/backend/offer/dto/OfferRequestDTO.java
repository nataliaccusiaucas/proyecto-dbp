package com.hirehub.backend.offer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OfferRequestDTO(
        @NotNull(message = "El presupuesto propuesto es obligatorio")
        Double proposedBudget,

        @NotBlank(message = "La propuesta no puede estar vacía")
        String proposalText,

        @NotNull(message = "Debe especificar el ID de la solicitud de trabajo")
        UUID jobRequestId,

        @NotNull(message = "Debe especificar el ID del freelancer")
        UUID freelancerId
) {}