package com.akhil.orders.service;

import com.akhil.orders.domain.valueobject.InventoryStatus;

public interface InventoryVerificationService{
    InventoryStatus verifyReservation(String reservationId);
}

