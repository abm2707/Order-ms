package com.akhil.orders.order_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrderRequest {

    @NotNull
    private UUID customerId;

    @NotNull
    @Positive
    private BigDecimal totalAmount;

    @NotNull
    private String currency;

    public UUID getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }
}
