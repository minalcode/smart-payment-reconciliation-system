package com.artificial.smart_payment_reconciliation_system.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentResponseDto {

    private Long id;

    private String transactionId;

    private BigDecimal amount;

    private String recordType;

    private String status;
}