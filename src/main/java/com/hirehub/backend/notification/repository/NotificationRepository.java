package com.hirehub.backend.notification.repository;

import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByRecipient(User recipient);
}
