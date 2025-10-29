package com.hirehub.backend.notification.controller;

import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.notification.service.NotificationService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @TestConfiguration
    static class MockBeans {
        @Bean NotificationService notificationService() { return Mockito.mock(NotificationService.class); }
        @Bean UserRepository userRepository() { return Mockito.mock(UserRepository.class); }
        @Bean JwtService jwtService() { return Mockito.mock(JwtService.class); }
        @Bean CustomUserDetailsService userDetailsService() { return Mockito.mock(CustomUserDetailsService.class); }
    }

    @Test
    @DisplayName("GET /api/notifications/user/{id} devuelve lista de notificaciones")
    void getNotifications_ok() throws Exception {
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("Ana");

        Notification n = new Notification();
        n.setId(UUID.randomUUID());
        n.setMessage("Nueva oferta");
        n.setRecipient(mockUser);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        when(notificationService.getNotificationsByUser(mockUser)).thenReturn(List.of(n));

        mockMvc.perform(get("/api/notifications/user/{userId}", userId))
                .andExpect(status().isOk());
    }
}