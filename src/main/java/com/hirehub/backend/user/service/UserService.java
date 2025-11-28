package com.hirehub.backend.user.service;

import com.hirehub.backend.common.exception.DuplicateResourceException;
import com.hirehub.backend.common.exception.ResourceNotFoundException;
import com.hirehub.backend.common.exception.UnauthorizedException;
import com.hirehub.backend.common.exception.ValidationException;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.dto.RegisterRequestDTO;
import com.hirehub.backend.user.dto.UserResponseDTO;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("El correo " + request.email() + " ya está registrado.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.name(),
                request.email(),
                request.phone(),
                request.role() != null
                        ? Role.valueOf(request.role().toUpperCase())
                        : Role.CLIENT,
                encodedPassword
        );

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

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

    public String updateAvatar(UUID userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("El archivo es obligatorio");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new ValidationException("Solo se permiten archivos de imagen");
        }

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth != null ? auth.getPrincipal() : null;
        boolean isAdmin = false;
        UUID currentUserId = null;
        if (principal instanceof User current) {
            currentUserId = current.getId();
            isAdmin = current.getRole() == Role.ADMIN;
        }

        if (!isAdmin && (currentUserId == null || !userId.equals(currentUserId))) {
            throw new UnauthorizedException("No puedes actualizar el avatar de otro usuario");
        }

        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }

        String filename = userId + "_" + System.currentTimeMillis() + ext;
        Path uploadDir = Paths.get("uploads", "avatars");
        try {
            Files.createDirectories(uploadDir);
            Path targetPath = uploadDir.resolve(filename).normalize();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }

        String publicUrl = "/uploads/avatars/" + filename;
        target.setAvatarUrl(publicUrl);
        userRepository.save(target);

        return publicUrl;
    }
}
