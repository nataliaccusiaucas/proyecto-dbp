package user;


import com.hirehub.backend.common.BaseIntegrationTest;
import com.hirehub.backend.
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest extends BaseIntegrationTest {
    @Autowired private UserRepository repository;

    @Test
    @DisplayName("shouldFindUserByEmailWhenEmailExists")
    void shouldFindUserByEmailWhenEmailExists() {
        User user = repository.save(new User("Test User", "test@example.com", null, Role.CLIENT, "password"));

        Optional<User> result = repository.findByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
    }

    @Test
    @DisplayName("shouldReturnEmptyWhenEmailDoesNotExist")
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> result = repository.findByEmail("nonexistent@example.com");
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("shouldFindUsersByRoleWhenRoleExists")
    void shouldFindUsersByRoleWhenRoleExists() {
        repository.save(new User("Client", "client@example.com", null, Role.CLIENT, "pass"));
        repository.save(new User("Freelancer", "free@example.com", null, Role.FREELANCER, "pass"));

        List<User> clients = repository.findByRole(Role.CLIENT);
        assertEquals(1, clients.size());
        assertEquals(Role.CLIENT, clients.get(0).getRole());
    }
}