package com.hirehub.backend.commission.controller;

import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.commission.service.CommissionInvoiceService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommissionInvoiceControllerTest {
    @Mock
    private CommissionInvoiceService commissionInvoiceService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CommissionInvoiceController(commissionInvoiceService)).build();
    }

    @Test
    @DisplayName("Should return invoices when freelancer exists")
    void shouldReturnInvoicesWhenFreelancerExists() throws Exception {
        UUID freelancerId = UUID.randomUUID();
        CommissionInvoice invoice = Mockito.mock(CommissionInvoice.class);
        UUID invoiceId = UUID.randomUUID();
        when(invoice.getId()).thenReturn(invoiceId);
        
        when(commissionInvoiceService.getInvoicesByFreelancer(any(UUID.class)))
                .thenReturn(List.of(invoice));

        mockMvc.perform(get("/api/commission-invoices/freelancer/{freelancerId}", freelancerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("Should mark invoice as paid successfully")
    void shouldMarkInvoiceAsPaidSuccessfully() throws Exception {
        UUID invoiceId = UUID.randomUUID();
        CommissionInvoice paidInvoice = Mockito.mock(CommissionInvoice.class);
        when(paidInvoice.getId()).thenReturn(invoiceId);
        when(commissionInvoiceService.markAsPaid(invoiceId)).thenReturn(paidInvoice);

        mockMvc.perform(patch("/api/commission-invoices/{invoiceId}/pay", invoiceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}