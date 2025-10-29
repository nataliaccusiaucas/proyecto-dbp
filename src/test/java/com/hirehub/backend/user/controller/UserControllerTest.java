package com.hirehub.backend.user.controller;

import com.hirehub.backend.user.dto.UserResponseDTO;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.service.UserService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class MockBeans {

        @Bean
        UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        JwtService jwtService() {
            return Mockito.mock(JwtService.class);
        }

        @Bean
        CustomUserDetailsService userDetailsService() {
            return Mockito.mock(CustomUserDetailsService.class);
        }
    }

    private UserResponseDTO dto(String name) {
        return new UserResponseDTO(
                UUID.randomUUID(),
                name,
                name.toLowerCase() + "@mail.com",
                "999999999",
                Role.CLIENT,
                LocalDateTime.now(),
                4.5,
                3
        );
    }

    @Test
    @DisplayName("GET /api/users devuelve lista de usuarios")
    void getAllUsers_returnsList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(dto("Ana"), dto("Beto")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ana"))
                .andExpect(jsonPath("$[1].name").value("Beto"));
    }

    @Test
    @DisplayName("GET /api/users/{id} devuelve un usuario por ID")
    void getUserById_returnsUser() throws Exception {
        UUID id = UUID.randomUUID();
        UserResponseDTO carlos = dto("Carlos");

        when(userService.getUserById(eq(id))).thenReturn(carlos);

        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@mail.com"));
    }
}