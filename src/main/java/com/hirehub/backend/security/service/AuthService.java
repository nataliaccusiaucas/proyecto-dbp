package com.hirehub.backend.security.service;

import com.hirehub.backend.common.exception.DuplicateResourceException;
import com.hirehub.backend.common.exception.ResourceNotFoundException;
import com.hirehub.backend.common.exception.UnauthorizedException;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.dto.AuthRequestDTO;
import com.hirehub.backend.user.dto.AuthResponseDTO;
import com.hirehub.backend.user.dto.RegisterRequestDTO;
import com.hirehub.backend.user.dto.UserResponseDTO;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("El email ya está registrado: " + request.email());
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setPassword(passwordEncoder.encode(request.password()));

        String roleValue = request.role() != null && !request.role().isBlank()
                ? request.role().toUpperCase()
                : "CLIENT";
        user.setRole(Role.valueOf(roleValue));

        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(
                new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole(),
                        user.getCreatedAt(),
                        user.getAverageRating(),
                        user.getCompletedJobs()
                ),
                token
        );
    }


    public AuthResponseDTO login(AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Credenciales incorrectas.");
        }

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con email: " + request.email()
                ));

        String token = jwtService.generateToken(user);

        return new AuthResponseDTO(
                mapToDTO(user),
                token
        );
    }

    private UserResponseDTO mapToDTO(User user) {
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
