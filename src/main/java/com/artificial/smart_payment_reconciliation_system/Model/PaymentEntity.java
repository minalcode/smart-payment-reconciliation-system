package com.artificial.smart_payment_reconciliation_system.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private BigDecimal amount;

    // COMPANY or BANK
    private String recordType;

    // PENDING, MATCHED, UNMATCHED, DUPLICATE
    private String status = "PENDING";

    private LocalDateTime createdAt = LocalDateTime.now();
}