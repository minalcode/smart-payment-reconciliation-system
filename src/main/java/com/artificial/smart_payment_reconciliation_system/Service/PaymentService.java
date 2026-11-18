
package com.artificial.smart_payment_reconciliation_system.Service;

import com.artificial.smart_payment_reconciliation_system.Dto.PaymentRequestDto;
import com.artificial.smart_payment_reconciliation_system.Dto.PaymentResponseDto;
import com.artificial.smart_payment_reconciliation_system.Model.PaymentEntity;
import com.artificial.smart_payment_reconciliation_system.Repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // Create Payment
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        if (request == null) {
            throw new RuntimeException("Request body is required");
        }

        if (request.getTransactionId() == null ||
                request.getTransactionId().isBlank()) {
            throw new RuntimeException("Transaction ID is required");
        }

        if (request.getAmount() == null ||
                request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }

        if (request.getRecordType() == null ||
                (!request.getRecordType().equalsIgnoreCase("COMPANY") &&
                        !request.getRecordType().equalsIgnoreCase("BANK"))) {
            throw new RuntimeException("Record type must be COMPANY or BANK");
        }

        PaymentEntity payment = new PaymentEntity();

        payment.setTransactionId(request.getTransactionId().trim());
        payment.setAmount(request.getAmount());
        payment.setRecordType(request.getRecordType().toUpperCase());
        payment.setStatus("PENDING");

        PaymentEntity saved = paymentRepository.save(payment);

        return convertToResponse(saved);
    }

    // Reconcile Payments
    @Transactional
    public List<PaymentResponseDto> reconcilePayments() {

        List<PaymentEntity> payments = paymentRepository.findAll();

        List<PaymentEntity> companyPayments = payments.stream()
                .filter(p -> "COMPANY".equals(p.getRecordType()))
                .toList();

        List<PaymentEntity> bankPayments = payments.stream()
                .filter(p -> "BANK".equals(p.getRecordType()))
                .toList();

        Set<Long> matchedBankIds = new HashSet<>();
        Set<String> seenBankTransactions = new HashSet<>();

        // Detect duplicate bank transactions
        for (PaymentEntity bank : bankPayments) {

            String key = bank.getTransactionId() + "_" + bank.getAmount();

            if (!seenBankTransactions.add(key)) {
                bank.setStatus("DUPLICATE");
            } else {
                bank.setStatus("UNMATCHED");
            }
        }

        // Match company and bank transactions
        for (PaymentEntity company : companyPayments) {

            company.setStatus("UNMATCHED");

            for (PaymentEntity bank : bankPayments) {

                if ("UNMATCHED".equals(bank.getStatus()) &&
                        !matchedBankIds.contains(bank.getId()) &&
                        company.getTransactionId().equals(bank.getTransactionId()) &&
                        company.getAmount().compareTo(bank.getAmount()) == 0) {

                    company.setStatus("MATCHED");
                    bank.setStatus("MATCHED");

                    matchedBankIds.add(bank.getId());

                    break;
                }
            }
        }

        paymentRepository.saveAll(payments);

        return payments.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get All Payments
    public List<PaymentResponseDto> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Convert Entity to Response DTO
    private PaymentResponseDto convertToResponse(PaymentEntity payment) {

        return new PaymentResponseDto(
                payment.getId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getRecordType(),
                payment.getStatus()
        );
    }
}
