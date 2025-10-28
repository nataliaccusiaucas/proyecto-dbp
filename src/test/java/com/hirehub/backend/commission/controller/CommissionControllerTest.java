package com.hirehub.backend.commission.controller;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.commission.domain.CommissionStatus;
import com.hirehub.backend.commission.service.CommissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommissionControllerTest {

    @Mock
    private CommissionService commissionService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommissionController(commissionService)).build();
    }

    @Test
    @DisplayName("Should return commissions when freelancer exists")
    void shouldReturnCommissionsWhenFreelancerExists() throws Exception {
        UUID freelancerId = UUID.randomUUID();
        Commission commission = Mockito.mock(Commission.class);
        UUID commissionId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        when(commission.getId()).thenReturn(commissionId);
        when(commission.getCreatedAt()).thenReturn(createdAt);
        when(commission.getStatus()).thenReturn(CommissionStatus.PENDING);
        when(commissionService.getCommissionsByFreelancer(any(UUID.class)))
                .thenReturn(List.of(commission));

        mockMvc.perform(get("/api/commissions/freelancer/{freelancerId}", freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Should mark commission as paid successfully")
    void shouldMarkCommissionAsPaidSuccessfully() throws Exception {
        UUID commissionId = UUID.randomUUID();
        Commission paidCommission = Mockito.mock(Commission.class);
        when(paidCommission.getId()).thenReturn(commissionId);
        when(paidCommission.getStatus()).thenReturn(CommissionStatus.PAID);
        when(commissionService.markAsPaid(commissionId)).thenReturn(paidCommission);

        mockMvc.perform(patch("/api/commissions/{commissionId}/pay", commissionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}