package com.akhil.orders.dto.response;

import com.akhil.orders.domain.valueobject.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {

    private UUID id;
    private String orderNumber;
    private UUID customerId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private Date createdAt;
    private List<OrderItemResponse> items;

    public OrderResponse(UUID id,
                         String orderNumber,
                         UUID customerId,
                         OrderStatus status,
                         BigDecimal totalAmount,
                         String currency,
                         Date createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.createdAt = createdAt;
        this.items = items;
    }
}
