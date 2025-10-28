package com.hirehub.backend.user.controller;

import com.hirehub.backend.user.dto.UserResponseDTO;
import com.hirehub.backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(service)).build();
    }

    @Test
    @DisplayName("shouldReturnUserWhenIdExists")
    void shouldReturnUserWhenIdExists() throws Exception {
        UserResponseDTO dto = new UserResponseDTO(UUID.randomUUID(), "Test", "test@example.com", null, null, null, null, null);
        when(service.getUserById(any())).thenReturn(dto);

        mockMvc.perform(get("/api/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    @DisplayName("shouldReturn400WhenUserNotFound")
    void shouldReturn400WhenUserNotFound() throws Exception {
        when(service.getUserById(any())).thenThrow(new IllegalArgumentException("Not found"));

        mockMvc.perform(get("/api/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}