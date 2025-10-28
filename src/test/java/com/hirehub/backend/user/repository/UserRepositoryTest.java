package com.hirehub.backend.user.repository;

import com.hirehub.backend.common.BaseIntegrationTest;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private UserRepository repository;

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
    @DisplayName("shouldFindUserByRoleUsingEmailCheck")
    void shouldFindUserByRoleUsingEmailCheck() {
        repository.save(new User("Client", "client@example.com", null, Role.CLIENT, "pass"));
        repository.save(new User("Freelancer", "free@example.com", null, Role.FREELANCER, "pass"));

        Optional<User> client = repository.findByEmail("client@example.com");
        assertTrue(client.isPresent());
        assertEquals(Role.CLIENT, client.get().getRole());
    }
}