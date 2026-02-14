package com.akhil.orders.service;

import com.akhil.orders.domain.valueobject.PaymentStatus;

public interface PaymentVerificationService {
    PaymentStatus verifyPayment(String paymentReferenceId);
}
