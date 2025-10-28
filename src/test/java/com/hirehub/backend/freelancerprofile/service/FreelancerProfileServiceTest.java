package com.hirehub.backend.freelancerprofile.service;

import com.hirehub.backend.freelancerprofile.domain.FreelancerProfile;
import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileResponseDTO;
import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileUpdateDTO;
import com.hirehub.backend.freelancerprofile.repository.FreelancerProfileRepository;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FreelancerProfileServiceTest {

    @Mock
    private FreelancerProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FreelancerProfileService freelancerProfileService;

    private User testUser;
    private FreelancerProfile testProfile;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setName("Test Freelancer");
        testUser.setAverageRating(4.5);
        testUser.setCompletedJobs(10);

        testProfile = new FreelancerProfile();
        testProfile.setUser(testUser);
        testProfile.setTitle("Senior Developer");
        testProfile.setDescription("Experienced developer with 5 years of experience");
        testProfile.setSkills("Java, Spring Boot, AWS");
        testProfile.setPortfolioUrl("https://portfolio.test");
        testProfile.setLocation("Lima, Peru");
    }

    @Test
    void getProfileByFreelancer_Success() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(profileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));

        FreelancerProfileResponseDTO response = freelancerProfileService.getProfileByFreelancer(testUserId);

        assertNotNull(response);
        assertEquals(testProfile.getId(), response.id());
        assertEquals(testUser.getName(), response.name());
        assertEquals(testProfile.getTitle(), response.title());
        assertEquals(testProfile.getDescription(), response.description());
        assertEquals(testProfile.getSkills(), response.skills());
        assertEquals(testProfile.getPortfolioUrl(), response.portfolioUrl());
        assertEquals(testProfile.getLocation(), response.location());
        assertEquals(testUser.getAverageRating(), response.averageRating());
        assertEquals(testUser.getCompletedJobs(), response.completedJobs());

        verify(userRepository).findById(testUserId);
        verify(profileRepository).findByUser(testUser);
    }

    @Test
    void getProfileByFreelancer_UserNotFound() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> freelancerProfileService.getProfileByFreelancer(testUserId));
        
        verify(userRepository).findById(testUserId);
        verify(profileRepository, never()).findByUser(any());
    }

    @Test
    void getProfileByFreelancer_ProfileNotFound() {
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(profileRepository.findByUser(testUser)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> freelancerProfileService.getProfileByFreelancer(testUserId));
        
        verify(userRepository).findById(testUserId);
        verify(profileRepository).findByUser(testUser);
    }

    @Test
    void updateProfile_ExistingProfile_Success() {
        FreelancerProfileUpdateDTO updateDTO = new FreelancerProfileUpdateDTO(
            "Updated Title",
            "Updated Description",
            "Java, Spring, React",
            "https://updated-portfolio.test",
            "Updated Location"
        );

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(profileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(FreelancerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FreelancerProfileResponseDTO response = freelancerProfileService.updateProfile(testUserId, updateDTO);

        assertNotNull(response);
        assertEquals(updateDTO.title(), response.title());
        assertEquals(updateDTO.description(), response.description());
        assertEquals(updateDTO.skills(), response.skills());
        assertEquals(updateDTO.portfolioUrl(), response.portfolioUrl());
        assertEquals(updateDTO.location(), response.location());

        verify(userRepository).findById(testUserId);
        verify(profileRepository).findByUser(testUser);
        verify(profileRepository).save(any(FreelancerProfile.class));
    }

    @Test
    void updateProfile_CreateNewProfile_Success() {
        FreelancerProfileUpdateDTO updateDTO = new FreelancerProfileUpdateDTO(
            "New Title",
            "New Description",
            "Java, Spring",
            "https://new-portfolio.test",
            "New Location"
        );

        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(profileRepository.findByUser(testUser)).thenReturn(Optional.empty());
        when(profileRepository.save(any(FreelancerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FreelancerProfileResponseDTO response = freelancerProfileService.updateProfile(testUserId, updateDTO);

        assertNotNull(response);
        assertEquals(updateDTO.title(), response.title());
        assertEquals(updateDTO.description(), response.description());
        assertEquals(updateDTO.skills(), response.skills());
        assertEquals(updateDTO.portfolioUrl(), response.portfolioUrl());
        assertEquals(updateDTO.location(), response.location());

        verify(userRepository).findById(testUserId);
        verify(profileRepository).findByUser(testUser);
        verify(profileRepository).save(any(FreelancerProfile.class));
    }

    @Test
    void updateProfile_UserNotFound() {
        FreelancerProfileUpdateDTO updateDTO = new FreelancerProfileUpdateDTO(
            "Title",
            "Description",
            "Java",
            "https://portfolio.test",
            "Location"
        );
        
        when(userRepository.findById(testUserId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
            () -> freelancerProfileService.updateProfile(testUserId, updateDTO));
        
        verify(userRepository).findById(testUserId);
        verify(profileRepository, never()).findByUser(any());
        verify(profileRepository, never()).save(any());
    }
}
