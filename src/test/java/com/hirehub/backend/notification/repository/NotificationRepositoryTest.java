package com.hirehub.backend.notification.repository;

import com.hirehub.backend.common.BaseIntegrationTest;
import com.hirehub.backend.notification.domain.Notification;
import com.hirehub.backend.notification.domain.NotificationType;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class NotificationRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("shouldFindNotificationsByUserWhenUserHasNotifications")
    void shouldFindNotificationsByUserWhenUserHasNotifications() {
    User user = userRepository.save(new User("User", "user@example.com", null, Role.CLIENT, "pass"));
    Notification notificationToSave = new Notification(NotificationType.OFFER_CREATED, "Test message", user);
    notificationToSave.setRead(false);
    Notification notification = repository.save(notificationToSave);

    List<Notification> result = repository.findByRecipient(user);
        assertEquals(1, result.size());
        assertEquals(notification.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("shouldFindUnreadNotificationsWhenUserHasUnread")
    void shouldFindUnreadNotificationsWhenUserHasUnread() {
    User user = userRepository.save(new User("User", "user@example.com", null, Role.CLIENT, "pass"));
    Notification unreadNotif = new Notification(NotificationType.OFFER_CREATED, "Unread", user);
    unreadNotif.setRead(false);
    repository.save(unreadNotif);
    Notification readNotif = new Notification(NotificationType.OFFER_CREATED, "Read", user);
    readNotif.setRead(true);
    repository.save(readNotif);

    List<Notification> unread = repository.findByRecipient(user).stream().filter(n -> !n.isRead()).toList();
        assertEquals(1, unread.size());
        assertFalse(unread.get(0).isRead());
    }
}
