package com.hirehub.backend.commission.service;

import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.commission.domain.InvoiceStatus;
import com.hirehub.backend.commission.repository.CommissionInvoiceRepository;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommissionInvoiceServiceTest {
    @Mock private CommissionInvoiceRepository invoiceRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private CommissionInvoiceService service;

    @Test
    @DisplayName("Should create new invoice when no pending invoice exists in current month")
    void shouldCreateNewInvoiceWhenNoPendingInvoiceExists() {
        UUID freelancerId = UUID.randomUUID();
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        Double amount = 100.0;

        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(invoiceRepository.findByFreelancer(freelancer)).thenReturn(Collections.emptyList());
        when(invoiceRepository.save(any(CommissionInvoice.class))).thenAnswer(i -> i.getArguments()[0]);

        CommissionInvoice result = service.createInvoice(freelancerId, amount);
        
        assertNotNull(result);
        assertEquals(amount, result.getAmount());
        assertEquals(InvoiceStatus.PENDING, result.getStatus());
        assertEquals(freelancer, result.getFreelancer());
        verify(invoiceRepository).save(any(CommissionInvoice.class));
    }

    @Test
    @DisplayName("Should update existing invoice when pending invoice exists in current month")
    void shouldUpdateExistingInvoiceWhenPendingInvoiceExists() {
        UUID freelancerId = UUID.randomUUID();
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        Double initialAmount = 100.0;
        Double additionalAmount = 50.0;

        CommissionInvoice existingInvoice = new CommissionInvoice(freelancer, initialAmount);

        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(invoiceRepository.findByFreelancer(freelancer)).thenReturn(List.of(existingInvoice));
        when(invoiceRepository.save(any(CommissionInvoice.class))).thenAnswer(i -> i.getArguments()[0]);

        CommissionInvoice result = service.createInvoice(freelancerId, additionalAmount);
        
        assertNotNull(result);
        assertEquals(initialAmount + additionalAmount, result.getAmount());
        assertEquals(InvoiceStatus.PENDING, result.getStatus());
        verify(invoiceRepository).save(any(CommissionInvoice.class));
    }

    @Test
    @DisplayName("Should mark invoice as paid successfully")
    void shouldMarkInvoiceAsPaidSuccessfully() {
        UUID invoiceId = UUID.randomUUID();
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        CommissionInvoice invoice = new CommissionInvoice(freelancer, 100.0);
        invoice.setStatus(InvoiceStatus.PENDING);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(CommissionInvoice.class))).thenAnswer(i -> i.getArguments()[0]);

        CommissionInvoice result = service.markAsPaid(invoiceId);
        
        assertEquals(InvoiceStatus.PAID, result.getStatus());
        verify(invoiceRepository).save(invoice);
    }

        @Test
    @DisplayName("Should get invoices by freelancer successfully")
    void shouldGetInvoicesByFreelancerSuccessfully() {
        UUID freelancerId = UUID.randomUUID();
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        CommissionInvoice invoice = new CommissionInvoice(freelancer, 100.0);

        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(invoiceRepository.findByFreelancer(freelancer)).thenReturn(List.of(invoice));

        List<CommissionInvoice> results = service.getInvoicesByFreelancer(freelancerId);
        
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(invoice, results.get(0));
        verify(invoiceRepository).findByFreelancer(freelancer);
    }

    @Test
    @DisplayName("Should throw exception when freelancer not found")
    void shouldThrowExceptionWhenFreelancerNotFound() {
        UUID freelancerId = UUID.randomUUID();
        when(userRepository.findById(freelancerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getInvoicesByFreelancer(freelancerId));
        verify(invoiceRepository, never()).findByFreelancer(any());
    }
}