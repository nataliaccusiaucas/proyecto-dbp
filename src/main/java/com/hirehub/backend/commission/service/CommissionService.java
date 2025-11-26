package com.hirehub.backend.commission.service;

import com.hirehub.backend.commission.domain.Commission;
import com.hirehub.backend.commission.domain.CommissionStatus;
import com.hirehub.backend.commission.repository.CommissionRepository;
import com.hirehub.backend.user.domain.User;
import com.hirehub.backend.jobrequest.domain.JobRequest;
import com.hirehub.backend.user.repository.UserRepository;
import com.hirehub.backend.jobrequest.repository.JobRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;
    private final CommissionInvoiceService commissionInvoiceService;
    private final UserRepository userRepository;
    private final JobRequestRepository jobRequestRepository;

    public CommissionService(
            CommissionRepository commissionRepository,
            CommissionInvoiceService commissionInvoiceService,
            UserRepository userRepository,
            JobRequestRepository jobRequestRepository) {
        this.commissionRepository = commissionRepository;
        this.commissionInvoiceService = commissionInvoiceService;
        this.userRepository = userRepository;
        this.jobRequestRepository = jobRequestRepository;
    }

    public Commission createCommission(User freelancer, JobRequest jobRequest, Double jobBudget) {
        double amount = jobBudget * 0.05;
        Commission commission = new Commission(freelancer, jobRequest, amount);
        commissionRepository.save(commission);

        commissionInvoiceService.createInvoice(freelancer.getId(), amount);

        return commission;
    }

    public List<Commission> getCommissionsByFreelancer(UUID freelancerId) {
        User freelancer = userRepository.findById(freelancerId)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer no encontrado"));
        return commissionRepository.findByFreelancer(freelancer);
    }

    public Commission markAsPaid(UUID commissionId) {
        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new IllegalArgumentException("Comisión no encontrada"));
        commission.setStatus(CommissionStatus.PAID);
        return commissionRepository.save(commission);
    }

    public List<Commission> getAllCommissions() {
        return commissionRepository.findAll();
    }
}
