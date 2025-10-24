package com.hirehub.backend.security.controller;

import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.dto.AuthRequestDTO;
import com.hirehub.backend.user.dto.AuthResponseDTO;
import com.hirehub.backend.user.dto.RegisterRequestDTO;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(new AuthResponseDTO("El correo ya está registrado", null));
        }

        User user = new User(
                request.name(),
                request.email(),
                request.phone(),
                request.role() != null ? request.role() : Role.CLIENT,
                passwordEncoder.encode(request.password())
        );

        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDTO("Registro exitoso", token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDTO("Login exitoso", token));
    }
}
