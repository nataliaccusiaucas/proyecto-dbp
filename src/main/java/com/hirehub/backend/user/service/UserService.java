package com.hirehub.backend.user.service;

import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.dto.UserRequestDTO;
import com.hirehub.backend.user.dto.UserResponseDTO;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.user.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Crear usuario desde DTO
    public UserResponseDTO createUser(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }

        User user = new User(
                request.name(),
                request.email(),
                request.phone(),
                request.role() != null ? request.role() : Role.CLIENT
        );

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    //  Obtener todos los usuarios como DTOs
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Buscar por ID
    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    //  Conversión de entidad → DTO
    private UserResponseDTO mapToResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt(),
                user.getAverageRating(),
                user.getCompletedJobs()
        );
    }
}
