package com.hirehub.backend.jobrequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record JobRequestRequestDTO(
        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "El presupuesto no puede ser nulo")
        Double budget,

        @NotNull(message = "Debe especificar el ID del cliente")
        UUID clientId
) {}
