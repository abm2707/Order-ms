package com.akhil.orders.domain.valueobject;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PAID,
    CANCELLED,
    SHIPPED,
    PENDING,
    PAYMENT_PENDING;

    public boolean isTerminal() {
        return this == CONFIRMED || this == CANCELLED;
    }

}
