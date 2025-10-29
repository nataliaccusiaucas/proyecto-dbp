package com.hirehub.backend.jobrequest.controller;

import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.dto.JobRequestResponseDTO;
import com.hirehub.backend.jobrequest.service.JobRequestService;
import com.hirehub.backend.security.jwt.JwtService;
import com.hirehub.backend.security.service.CustomUserDetailsService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = JobRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobRequestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private JobRequestService jobRequestService;

    @TestConfiguration
    static class MockBeans {
        @Bean JobRequestService jobRequestService() { return Mockito.mock(JobRequestService.class); }
        @Bean JwtService jwtService() { return Mockito.mock(JwtService.class); }
        @Bean CustomUserDetailsService userDetailsService() { return Mockito.mock(CustomUserDetailsService.class); }
    }

    private JobRequestResponseDTO dto(String title, String client) {
        return new JobRequestResponseDTO(
                UUID.randomUUID(), title, "desc", 500.0, JobStatus.OPEN,
                UUID.randomUUID(), client, LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("GET /api/job-requests devuelve lista")
    void getAll_returnsList() throws Exception {
        when(jobRequestService.getAllJobRequests()).thenReturn(List.of());

        mockMvc.perform(get("/api/job-requests"))
                .andExpect(status().isOk());
    }
}
