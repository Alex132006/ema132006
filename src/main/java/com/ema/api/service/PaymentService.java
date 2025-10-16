package com.ema.api.service;

import com.ema.api.model.Payment;
import com.ema.api.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final Random random = new Random();

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        // In a real app, this would be handled by the RideService
        return paymentRepository.save(payment);
    }

    public Payment processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + paymentId));

        if (!"PENDING".equals(payment.getStatus())) {
            throw new IllegalStateException("Payment is not in PENDING state");
        }

        // Simulate an external payment gateway call
        boolean paymentSucceeded = random.nextBoolean();

        if (paymentSucceeded) {
            payment.setStatus("COMPLETED");
        } else {
            payment.setStatus("FAILED");
        }

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPendingPayments() {
        return paymentRepository.findByStatus("PENDING");
    }
}
