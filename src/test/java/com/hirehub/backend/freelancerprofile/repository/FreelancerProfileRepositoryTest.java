package com.hirehub.backend.freelancerprofile.repository;

import com.hirehub.backend.freelancerprofile.domain.FreelancerProfile;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class FreelancerProfileRepositoryTest {
    @Autowired
    private FreelancerProfileRepository freelancerProfileRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find profile when user exists")
    void shouldFindProfileWhenUserExists() {
        User user = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        user = userRepository.save(user);
        
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setTitle("Senior Developer");
        profile.setDescription("Professional software developer with 5+ years experience");
        profile.setSkills("Java, Spring Boot, SQL");
        profile.setLocation("Remote");
        profile.setPortfolioUrl("https://portfolio.example.com");
        profile = freelancerProfileRepository.save(profile);

        Optional<FreelancerProfile> result = freelancerProfileRepository.findByUser(user);

        assertTrue(result.isPresent(), "Profile should be found");
        FreelancerProfile foundProfile = result.get();
        assertEquals(profile.getId(), foundProfile.getId(), "Profile ID should match");
        assertEquals(profile.getTitle(), foundProfile.getTitle(), "Title should match");
        assertEquals(profile.getDescription(), foundProfile.getDescription(), "Description should match");
        assertEquals(profile.getSkills(), foundProfile.getSkills(), "Skills should match");
        assertEquals(profile.getLocation(), foundProfile.getLocation(), "Location should match");
        assertEquals(profile.getPortfolioUrl(), foundProfile.getPortfolioUrl(), "Portfolio URL should match");
        assertEquals(user.getId(), foundProfile.getUser().getId(), "User ID should match");
    }

    @Test
    @DisplayName("Should return empty when user has no profile")
    void shouldReturnEmptyWhenUserHasNoProfile() {
        User user = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        user = userRepository.save(user);

        Optional<FreelancerProfile> result = freelancerProfileRepository.findByUser(user);

        assertFalse(result.isPresent(), "No profile should be found");
    }

    @Test
    @DisplayName("Should save profile with all fields")
    void shouldSaveProfileWithAllFields() {
        User user = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        user = userRepository.save(user);
        
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setTitle("Senior Developer");
        profile.setDescription("Professional software developer with 5+ years experience");
        profile.setSkills("Java, Spring Boot, SQL");
        profile.setLocation("Remote");
        profile.setPortfolioUrl("https://portfolio.example.com");

        FreelancerProfile savedProfile = freelancerProfileRepository.save(profile);

        assertNotNull(savedProfile.getId(), "Profile should have an ID after saving");
        assertEquals(profile.getTitle(), savedProfile.getTitle(), "Title should match");
        assertEquals(profile.getDescription(), savedProfile.getDescription(), "Description should match");
        assertEquals(profile.getSkills(), savedProfile.getSkills(), "Skills should match");
        assertEquals(profile.getLocation(), savedProfile.getLocation(), "Location should match");
        assertEquals(profile.getPortfolioUrl(), savedProfile.getPortfolioUrl(), "Portfolio URL should match");
        assertEquals(user.getId(), savedProfile.getUser().getId(), "User ID should match");
    }
}