package com.hirehub.backend.commission.scheduler;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.commission.domain.CommissionStatus;
import com.hirehub.backend.commission.repository.CommissionRepository;
import com.hirehub.backend.commission.service.CommissionInvoiceService;
import com.hirehub.backend.user.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MonthlyInvoiceScheduler {

    private final CommissionRepository commissionRepository;
    private final CommissionInvoiceService commissionInvoiceService;

    public MonthlyInvoiceScheduler(CommissionRepository commissionRepository,
                                   CommissionInvoiceService commissionInvoiceService) {
        this.commissionRepository = commissionRepository;
        this.commissionInvoiceService = commissionInvoiceService;
    }


    @Scheduled(cron = "0 0 3 1 * *") 
    public void generateMonthlyInvoices() {
        YearMonth lastMonth = YearMonth.now();
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        System.out.println("Generando facturas automáticas del mes " + lastMonth);

        List<Commission> monthlyCommissions = commissionRepository.findAll().stream()
                .filter(c -> c.getCreatedAt().toLocalDate().isAfter(start.minusDays(1))
                        && c.getCreatedAt().toLocalDate().isBefore(end.plusDays(1))
                        && c.getStatus() == CommissionStatus.PENDING)
                .collect(Collectors.toList());

        Map<User, List<Commission>> commissionsByFreelancer = monthlyCommissions.stream()
                .collect(Collectors.groupingBy(Commission::getFreelancer));

        commissionsByFreelancer.forEach((freelancer, commissions) -> {
            double total = commissions.stream()
                    .mapToDouble(Commission::getAmount)
                    .sum();

            commissionInvoiceService.createInvoice(freelancer.getId(), total);
            System.out.println("Factura generada para " + freelancer.getName() + " por S/ " + total);
        });
    }
}
