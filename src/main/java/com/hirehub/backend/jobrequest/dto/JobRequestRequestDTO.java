package com.hirehub.backend.jobrequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record JobRequestRequestDTO(

        @NotNull(message = "Las categorías son obligatorias")
        List<String> categories,

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "El presupuesto es obligatorio")
        Double budget,

        @NotNull(message = "Debe especificar el ID del cliente")
        UUID clientId
) {}