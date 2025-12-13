package com.akhil.orders.order_service.service;

import com.akhil.orders.order_service.domain.entity.Order;
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
