package com.hirehub.backend.notification.service;

import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.notification.domain.NotificationType;
import com.hirehub.backend.notification.repository.NotificationRepository;
import com.hirehub.backend.user.domain.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(NotificationType type, String message, User recipient) {
        Notification notification = new Notification(type, message, recipient);
        return notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(User user) {
        return notificationRepository.findByRecipient(user);
    }

    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada."));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
