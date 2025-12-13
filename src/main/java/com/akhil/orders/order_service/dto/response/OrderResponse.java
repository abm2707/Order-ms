package com.akhil.orders.order_service.dto.response;

import com.akhil.orders.order_service.domain.valueobject.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderResponse {

    private UUID id;
    private String orderNumber;
    private UUID customerId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private Instant createdAt;

    public OrderResponse(UUID id,
                         String orderNumber,
                         UUID customerId,
                         OrderStatus status,
                         BigDecimal totalAmount,
                         String currency,
                         Instant createdAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.createdAt = createdAt;
    }
}
