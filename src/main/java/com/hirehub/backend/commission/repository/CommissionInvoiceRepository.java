package com.hirehub.backend.commission.repository;

import com.hirehub.backend.commission.domain.CommissionInvoice;
import com.hirehub.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommissionInvoiceRepository extends JpaRepository<CommissionInvoice, UUID> {
    List<CommissionInvoice> findByFreelancer(User freelancer);
}
