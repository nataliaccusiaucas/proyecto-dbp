package com.hirehub.backend.commission.service;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.commission.domain.CommissionStatus;
import com.hirehub.backend.commission.repository.CommissionRepository;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.jobrequest.domain.JobStatus;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommissionServiceTest {
    @Mock private CommissionRepository commissionRepository;
    @Mock private CommissionInvoiceService commissionInvoiceService;
    @Mock private UserRepository userRepository;
    @Mock private JobRequestRepository jobRequestRepository;
    @InjectMocks private CommissionService service;

    @Test
    @DisplayName("Should create commission and invoice when freelancer and job exist")
    void shouldCreateCommissionAndInvoiceWhenFreelancerAndJobExist() {
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        JobRequest job = new JobRequest("Job", "Desc", 100.0, JobStatus.OPEN, freelancer);
        Double jobBudget = 100.0;
        Double expectedCommissionAmount = jobBudget * 0.05;

        when(commissionRepository.save(any(Commission.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Commission result = service.createCommission(freelancer, job, jobBudget);
        
        assertNotNull(result);
        assertEquals(expectedCommissionAmount, result.getAmount());
        assertEquals(freelancer, result.getFreelancer());
        assertEquals(job, result.getJobRequest());
        assertEquals(CommissionStatus.PENDING, result.getStatus());
        verify(commissionRepository).save(any(Commission.class));
        verify(commissionInvoiceService).createInvoice(freelancer.getId(), expectedCommissionAmount);
    }

    @Test
    @DisplayName("Should get commissions when freelancer exists")
    void shouldGetCommissionsWhenFreelancerExists() {
        UUID freelancerId = UUID.randomUUID();
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        Commission commission = new Commission(freelancer, new JobRequest(), 5.0);
        
        when(userRepository.findById(freelancerId)).thenReturn(Optional.of(freelancer));
        when(commissionRepository.findByFreelancer(freelancer)).thenReturn(List.of(commission));

        List<Commission> results = service.getCommissionsByFreelancer(freelancerId);
        
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(commission, results.get(0));
        verify(commissionRepository).findByFreelancer(freelancer);
    }

    @Test
    @DisplayName("Should throw exception when freelancer not found for get commissions")
    void shouldThrowExceptionWhenFreelancerNotFoundForGetCommissions() {
        UUID freelancerId = UUID.randomUUID();
        when(userRepository.findById(freelancerId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getCommissionsByFreelancer(freelancerId));
        verify(commissionRepository, never()).findByFreelancer(any());
    }

    @Test
    @DisplayName("Should mark commission as paid successfully")
    void shouldMarkCommissionAsPaidSuccessfully() {
        UUID commissionId = UUID.randomUUID();
        User freelancer = new User("Free", "free@example.com", null, Role.FREELANCER, "pass");
        Commission commission = new Commission(freelancer, new JobRequest(), 5.0);
        commission.setStatus(CommissionStatus.PENDING);

        when(commissionRepository.findById(commissionId)).thenReturn(Optional.of(commission));
        when(commissionRepository.save(any(Commission.class))).thenAnswer(i -> i.getArguments()[0]);

        Commission result = service.markAsPaid(commissionId);
        
        assertEquals(CommissionStatus.PAID, result.getStatus());
        verify(commissionRepository).save(commission);
    }

    @Test
    @DisplayName("Should throw exception when commission not found for marking as paid")
    void shouldThrowExceptionWhenCommissionNotFoundForMarkingAsPaid() {
        UUID commissionId = UUID.randomUUID();
        when(commissionRepository.findById(commissionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.markAsPaid(commissionId));
        verify(commissionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get all commissions")
    void shouldGetAllCommissions() {
        Commission commission = new Commission(new User(), new JobRequest(), 5.0);
        when(commissionRepository.findAll()).thenReturn(List.of(commission));

        List<Commission> results = service.getAllCommissions();
        
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(commission, results.get(0));
        verify(commissionRepository).findAll();
    }
}