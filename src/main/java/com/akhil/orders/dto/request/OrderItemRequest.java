package com.akhil.orders.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemRequest {

    @NotNull
    private UUID productId;

    @Min(1)
    private int quantity;

    @NotNull
    private BigDecimal mrp;

    @NotNull
    private BigDecimal rate; // selling price per unit

    private BigDecimal discount; // optional

    @NotNull
    private BigDecimal finalPrice; // post-discount * quantity
}

