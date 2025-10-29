package com.hirehub.backend.freelancerprofile.controller;

import com.hirehub.backend.freelancerprofile.dto.FreelancerProfileResponseDTO;
import com.hirehub.backend.freelancerprofile.service.FreelancerProfileService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FreelancerProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class FreelancerProfileControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private FreelancerProfileService profileService;

    @TestConfiguration
    static class MockBeans {
        @Bean FreelancerProfileService profileService() { return Mockito.mock(FreelancerProfileService.class); }
        @Bean JwtService jwtService() { return Mockito.mock(JwtService.class); }
        @Bean CustomUserDetailsService userDetailsService() { return Mockito.mock(CustomUserDetailsService.class); }
    }

    @Test
    void getProfile_ok() throws Exception {
        UUID id = UUID.randomUUID();
        FreelancerProfileResponseDTO dto = new FreelancerProfileResponseDTO(
                UUID.randomUUID(), "Ana", "Diseñadora", "UX/UI", "Figma, Notion",
                "port.com", "Lima", 4.9, 5
        );

        when(profileService.getProfileByFreelancer(eq(id))).thenReturn(dto);

        mockMvc.perform(get("/api/freelancers/{id}/profile", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana"));
    }
}