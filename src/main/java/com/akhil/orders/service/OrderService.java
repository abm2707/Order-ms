package com.akhil.orders.service;

import com.akhil.orders.domain.entity.Order;
import java.math.BigDecimal;
import java.util.UUID;

public interface OrderService {

    Order createOrder(
            UUID customerId,
            BigDecimal totalAmount,
            String currency
    );

    Order confirmOrder(UUID orderId);

    Order markOrderPaid(UUID orderId);

    Order cancelOrder(UUID orderId);
}
