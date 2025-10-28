package user.repository;

import com.hirehub.backend.common.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class NotificationRepositoryTest extends BaseIntegrationTest {
    @Autowired private NotificationRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("shouldFindNotificationsByUserWhenUserHasNotifications")
    void shouldFindNotificationsByUserWhenUserHasNotifications() {
        User user = userRepository.save(new User("User", "user@example.com", null, Role.CLIENT, "pass"));
        Notification notification = repository.save(new Notification(user, "Test message", NotificationType.INFO, false));

        List<Notification> result = repository.findByUser(user);
        assertEquals(1, result.size());
        assertEquals(notification.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("shouldFindUnreadNotificationsWhenUserHasUnread")
    void shouldFindUnreadNotificationsWhenUserHasUnread() {
        User user = userRepository.save(new User("User", "user@example.com", null, Role.CLIENT, "pass"));
        repository.save(new Notification(user, "Unread", NotificationType.INFO, false));
        repository.save(new Notification(user, "Read", NotificationType.INFO, true));

        List<Notification> unread = repository.findByUserAndIsRead(user, false);
        assertEquals(1, unread.size());
        assertFalse(unread.get(0).isRead());
    }
}
