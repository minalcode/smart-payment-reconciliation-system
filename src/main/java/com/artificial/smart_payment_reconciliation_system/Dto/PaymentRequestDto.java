package com.artificial.smart_payment_reconciliation_system.Dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    private String transactionId;

    private BigDecimal amount;

    private String recordType;
}