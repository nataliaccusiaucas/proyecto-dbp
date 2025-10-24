package com.hirehub.backend.security.service;

import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.dto.RegisterRequestDTO;
import com.hirehub.backend.user.dto.AuthResponseDTO;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
            User user = new User();
            user.setName(request.name());
            user.setEmail(request.email());
            user.setPhone(request.phone());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.valueOf(request.role().toUpperCase()));


        userRepository.save(user);

        return new AuthResponseDTO("Usuario registrado correctamente: " + user.getEmail());
    }
}
