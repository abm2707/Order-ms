package com.akhil.orders.service.Impl;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.dto.request.CreateOrderRequest;
import com.akhil.orders.event.OrderEventPublisher;
import com.akhil.orders.repository.OrderRepository;
import com.akhil.orders.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.akhil.common.events.OrderCreatedEvent;
import org.akhil.common.events.OrderItemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Autowired
    OrderEventPublisher eventPublisher;


    public OrderServiceImpl(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {

        String orderNumber = generateOrderNumber();
        
        Order order = new Order(
                orderNumber,
                request.getCustomerId(),
                request.getTotalAmount(),
                request.getCurrency(),
                request.getPaymentMethod()
        );

        request.getItems().forEach(item ->
                order.addItem(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getMrp(),
                        item.getRate(),
                        item.getDiscount(),
                        item.getFinalPrice()
                )
        );

        Order savedOrder = orderRepository.save(order);

        List<OrderItemEvent> itemEvents = savedOrder.getItems().stream().map(item -> new OrderItemEvent(
                item.getProductId(),
                item.getQuantity()
        )).toList();

        eventPublisher.publishOrderCreated(
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        savedOrder.getCustomerId(),
                        savedOrder.getTotalAmount(),
                        savedOrder.getCurrency(),
                        savedOrder.getCreatedAt(),
                        itemEvents
                )
        );

        log.info("pushing Order Details to Inventory via Kafka {}", savedOrder.getId());

        return savedOrder;
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
