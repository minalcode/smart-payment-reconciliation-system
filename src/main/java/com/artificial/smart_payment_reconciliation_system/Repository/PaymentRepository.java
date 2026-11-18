package com.artificial.smart_payment_reconciliation_system.Repository;

import com.artificial.smart_payment_reconciliation_system.Model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
}