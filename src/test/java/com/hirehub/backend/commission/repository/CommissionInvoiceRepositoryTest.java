package com.hirehub.backend.commission.repository;


import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.commission.domain.InvoiceStatus;
import com.hirehub.backend.common.BaseIntegrationTest;
import com.hirehub.backend.user.domain.Role;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CommissionInvoiceRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private CommissionInvoiceRepository repository;
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("shouldFindInvoicesByFreelancerWhenFreelancerExists")
    void shouldFindInvoicesByFreelancerWhenFreelancerExists() {
        User freelancer = userRepository.save(new User("Free", "free@example.com", null, Role.FREELANCER, "pass"));
        CommissionInvoice invoiceToSave = new CommissionInvoice(freelancer, 100.0);
        invoiceToSave.setDueDate(LocalDateTime.now().plusDays(30));
        invoiceToSave.setStatus(InvoiceStatus.PENDING);
        CommissionInvoice invoice = repository.save(invoiceToSave);

        List<CommissionInvoice> result = repository.findByFreelancer(freelancer);
        assertEquals(1, result.size());
        assertEquals(invoice.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("shouldFindInvoicesByStatusWhenStatusExists")
    void shouldFindInvoicesByStatusWhenStatusExists() {
        User freelancer = userRepository.save(new User("Free", "free@example.com", null, Role.FREELANCER, "pass"));
        CommissionInvoice paid = new CommissionInvoice(freelancer, 100.0);
        paid.setStatus(InvoiceStatus.PAID);
        repository.save(paid);

        CommissionInvoice pending = new CommissionInvoice(freelancer, 200.0);
        pending.setStatus(InvoiceStatus.PENDING);
        repository.save(pending);

        List<CommissionInvoice> pendingList = repository.findByFreelancer(freelancer).stream()
                .filter(inv -> inv.getStatus() == InvoiceStatus.PENDING)
                .toList();
        assertEquals(1, pendingList.size());
        assertEquals(InvoiceStatus.PENDING, pendingList.get(0).getStatus());
    }
}