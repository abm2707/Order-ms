package com.akhil.orders.service.Impl;

import com.akhil.orders.domain.valueobject.PaymentStatus;
import com.akhil.orders.service.PaymentVerificationService;
import org.springframework.stereotype.Service;

@Service
public class PaymentVerificationServiceStub
        implements PaymentVerificationService {

    @Override
    public PaymentStatus verifyPayment(String paymentReferenceId) {
        if (paymentReferenceId == null) {
            return PaymentStatus.NOT_FOUND;
        }
        return PaymentStatus.PENDING; // stub
    }
}

