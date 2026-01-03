package com.akhil.orders.service;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.dto.request.CreateOrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.UUID;

public interface OrderService {

    Order createOrder(CreateOrderRequest request) throws JsonProcessingException;

    Order confirmOrder(UUID orderId);

    Order markOrderPaid(UUID orderId);

    Order cancelOrder(UUID orderId);
}
