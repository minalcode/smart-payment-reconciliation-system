package com.artificial.smart_payment_reconciliation_system.Controller;

import com.artificial.smart_payment_reconciliation_system.Dto.PaymentRequestDto;
import com.artificial.smart_payment_reconciliation_system.Dto.PaymentResponseDto;
import com.artificial.smart_payment_reconciliation_system.Service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create Payment
    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @RequestBody PaymentRequestDto request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.createPayment(request));
    }

    // Reconcile Payments
    @PostMapping("/reconcile")
    public ResponseEntity<List<PaymentResponseDto>> reconcilePayments() {

        return ResponseEntity.ok(paymentService.reconcilePayments());
    }

    // Get All Payments
    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> getAllPayments() {

        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}