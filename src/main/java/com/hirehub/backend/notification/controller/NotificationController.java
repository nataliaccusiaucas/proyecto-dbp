package com.hirehub.backend.notification.controller;

import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.notification.service.NotificationService;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        return notificationService.getNotificationsByUser(user);
    }

    @PatchMapping("/{notificationId}/read")
    public void markNotificationAsRead(@PathVariable UUID notificationId) {
        notificationService.markAsRead(notificationId);
    }
}