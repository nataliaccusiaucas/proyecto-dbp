package com.hirehub.backend.security.controller;

import com.hirehub.backend.security.service.AuthService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;
import com.hirehub.backend.user.dto.AuthRequestDTO;
import com.hirehub.backend.user.dto.AuthResponseDTO;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AuthService authService;

    @TestConfiguration
    static class MockBeans {
        @Bean AuthService authService() { return Mockito.mock(AuthService.class); }
        @Bean JwtService jwtService() { return Mockito.mock(JwtService.class); }
        @Bean CustomUserDetailsService userDetailsService() { return Mockito.mock(CustomUserDetailsService.class); }
    }

    @Test
    @DisplayName("POST /api/auth/login responde correctamente")
    void login_ok() throws Exception {
        when(authService.login(any(AuthRequestDTO.class)))
                .thenReturn(new AuthResponseDTO("Login exitoso", "fake-token"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"email\":\"test@mail.com\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login exitoso"));
    }
}