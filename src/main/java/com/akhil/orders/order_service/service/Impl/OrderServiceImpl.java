package com.akhil.orders.order_service.service.Impl;

import com.akhil.orders.order_service.domain.entity.Order;
import com.akhil.orders.order_service.repository.OrderRepository;
import com.akhil.orders.order_service.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(UUID customerId,
                             BigDecimal totalAmount,
                             String currency) {

        String orderNumber = generateOrderNumber();

        Order order = new Order(
                orderNumber,
                customerId,
                totalAmount,
                currency
        );

        return orderRepository.save(order);
    }

    @Override
    public Order confirmOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.confirm();
        return order;
    }

    @Override
    public Order markOrderPaid(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.markPaid();
        return order;
    }

    @Override
    public Order cancelOrder(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.cancel();
        return order;
    }

    /* ======================
       Helper Methods
       ====================== */

    private Order getOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found: " + orderId
                        )
                );
    }

    private String generateOrderNumber() {
        // Simple for now — will be improved later
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
