package com.akhil.orders.service.Impl;

import com.akhil.orders.domain.valueobject.InventoryStatus;
import com.akhil.orders.service.InventoryVerificationService;
import org.springframework.stereotype.Service;

@Service
public class InventoryVerificationServiceStub
        implements InventoryVerificationService {

    @Override
    public InventoryStatus verifyReservation(String reservationId) {
        if (reservationId == null) {
            return InventoryStatus.NOT_FOUND;
        }
        return InventoryStatus.RESERVED; // stub
    }
}

