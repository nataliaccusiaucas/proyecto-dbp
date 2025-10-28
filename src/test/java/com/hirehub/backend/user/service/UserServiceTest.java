package com.hirehub.backend.user.service;


import com.hirehub.backend.user.dto.RegisterRequestDTO;
import com.hirehub.backend.user.dto.UserResponseDTO;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService service;

    @Test
    @DisplayName("shouldCreateUserWhenEmailIsUnique")
    void shouldCreateUserWhenEmailIsUnique() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Test", "test@example.com", null, "password", "CLIENT");
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDTO result = service.createUser(dto);
        assertNotNull(result);
        assertEquals(dto.email(), result.email());
        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("shouldThrowExceptionWhenEmailAlreadyExists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO("Test", "test@example.com", null, "password", "CLIENT");
        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.createUser(dto));
    }
}