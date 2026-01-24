package com.akhil.orders.mapper;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.dto.response.OrderItemResponse;
import com.akhil.orders.dto.response.OrderResponse;

import java.util.List;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems().stream().map(item -> new OrderItemResponse(
                item.getProductId(),
                item.getQuantity(),
                item.getMrp(),
                item.getRate(),
                item.getDiscount(),
                item.getFinalPrice()
        )).toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCurrency(),
                order.getCreatedAt(),
                items
        );
    }
}
