package com.hirehub.backend.user.dto;

import com.hirehub.backend.user.domain.Role;
import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @Email(message = "El correo no es válido")
        String email,

        @NotBlank(message = "El teléfono no puede estar vacío")
        String phone,
        
        Role role
) {}
