package com.akhil.orders.order_service.mapper;

import com.akhil.orders.order_service.domain.entity.Order;
import com.akhil.orders.order_service.dto.response.OrderResponse;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getCreatedAt()
        );
    }
}
