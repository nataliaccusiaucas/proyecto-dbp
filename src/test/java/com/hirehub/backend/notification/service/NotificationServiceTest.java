package com.hirehub.backend.notification.service;

import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.notification.domain.NotificationType;
import com.hirehub.backend.notification.repository.NotificationRepository;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;
    
    @InjectMocks
    private NotificationService notificationService;

    @Test
    @DisplayName("Should create notification successfully")
    void shouldCreateNotificationSuccessfully() {
        User recipient = new User("User", "user@example.com", null, Role.CLIENT, "pass");
        String message = "Test notification message";
        NotificationType type = NotificationType.OFFER_CREATED;
        
        when(notificationRepository.save(any(Notification.class)))
            .thenAnswer(i -> i.getArguments()[0]);

        Notification result = notificationService.createNotification(type, message, recipient);

        assertNotNull(result);
        assertEquals(message, result.getMessage());
        assertEquals(type, result.getType());
        assertEquals(recipient, result.getRecipient());
        assertFalse(result.isRead());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should get notifications by user")
    void shouldGetNotificationsByUser() {
        // Arrange
        User recipient = new User("User", "user@example.com", null, Role.CLIENT, "pass");
        Notification notification = new Notification(NotificationType.OFFER_CREATED, "Test message", recipient);
        
        when(notificationRepository.findByRecipient(recipient))
            .thenReturn(List.of(notification));

        List<Notification> results = notificationService.getNotificationsByUser(recipient);


        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(notification.getMessage(), results.get(0).getMessage());
        assertEquals(notification.getType(), results.get(0).getType());
        assertEquals(recipient, results.get(0).getRecipient());
        verify(notificationRepository).findByRecipient(recipient);
    }

    @Test
    @DisplayName("Should mark notification as read successfully")
    void shouldMarkNotificationAsReadSuccessfully() {
        UUID notificationId = UUID.randomUUID();
        User recipient = new User("User", "user@example.com", null, Role.CLIENT, "pass");
        Notification notification = new Notification(NotificationType.OFFER_CREATED, "Test message", recipient);
        
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(i -> i.getArguments()[0]);

        notificationService.markAsRead(notificationId);

        assertTrue(notification.isRead());
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(notification);
    }

    @Test
    @DisplayName("Should throw exception when notification not found for marking as read")
    void shouldThrowExceptionWhenNotificationNotFoundForMarkingAsRead() {
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> notificationService.markAsRead(notificationId));
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository, never()).save(any());
    }
}