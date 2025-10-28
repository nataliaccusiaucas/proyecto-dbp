package com.hirehub.backend.notification.controller;

import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.notification.domain.NotificationType;
import com.hirehub.backend.notification.service.NotificationService;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private UserRepository userRepository;
    
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        NotificationController controller = new NotificationController(notificationService, userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Should return notifications when user exists")
    void shouldReturnNotificationsWhenUserExists() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User("Test", "test@example.com", null, Role.FREELANCER, "pass");
        Notification notification = new Notification(NotificationType.OFFER_CREATED, "Test notification", user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationService.getNotificationsByUser(user)).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].message").value(notification.getMessage()));

        verify(userRepository).findById(userId);
        verify(notificationService).getNotificationsByUser(user);
    }

    @Test
    @DisplayName("Should return 400 when user not found")
    void shouldReturn400WhenUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notifications/user/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(userRepository).findById(userId);
        verify(notificationService, never()).getNotificationsByUser(any());
    }

    @Test
    @DisplayName("Should mark notification as read successfully")
    void shouldMarkNotificationAsReadSuccessfully() throws Exception {
        UUID notificationId = UUID.randomUUID();
        doNothing().when(notificationService).markAsRead(notificationId);

        mockMvc.perform(patch("/api/notifications/{notificationId}/read", notificationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(notificationService).markAsRead(notificationId);
    }
}