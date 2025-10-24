package com.hirehub.backend.commission.service;

import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.commission.domain.InvoiceStatus;
import com.hirehub.backend.commission.repository.CommissionInvoiceRepository;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class CommissionInvoiceService {

    private final CommissionInvoiceRepository commissionInvoiceRepository;
    private final UserRepository userRepository;

    public CommissionInvoiceService(CommissionInvoiceRepository commissionInvoiceRepository, UserRepository userRepository) {
        this.commissionInvoiceRepository = commissionInvoiceRepository;
        this.userRepository = userRepository;
    }

    public CommissionInvoice createInvoice(UUID freelancerId, Double newAmount) {
        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado."));

        YearMonth currentMonth = YearMonth.now();
        LocalDate startOfMonth = currentMonth.atDay(1);
        LocalDate endOfMonth = currentMonth.atEndOfMonth();

        List<CommissionInvoice> existingInvoices = commissionInvoiceRepository.findByFreelancer(freelancer)
                .stream()
                .filter(inv -> inv.getStatus() == InvoiceStatus.PENDING
                        && inv.getIssuedAt().toLocalDate().isAfter(startOfMonth.minusDays(1))
                        && inv.getIssuedAt().toLocalDate().isBefore(endOfMonth.plusDays(1)))
                .toList();

        CommissionInvoice invoice;

        if (!existingInvoices.isEmpty()) {
            invoice = existingInvoices.get(0);
            invoice.setAmount(invoice.getAmount() + newAmount);
        } else {
            invoice = new CommissionInvoice(freelancer, newAmount);
            invoice.setDueDate(endOfMonth.atTime(23, 59)); 
        }

        return commissionInvoiceRepository.save(invoice);
    }

    public CommissionInvoice markAsPaid(UUID invoiceId) {
        CommissionInvoice invoice = commissionInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada."));
        invoice.setStatus(InvoiceStatus.PAID);
        return commissionInvoiceRepository.save(invoice);
    }

    public List<CommissionInvoice> getInvoicesByFreelancer(UUID freelancerId) {
        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado."));
        return commissionInvoiceRepository.findByFreelancer(freelancer);
    }

    
}
