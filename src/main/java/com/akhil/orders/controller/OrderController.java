package com.akhil.orders.controller;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.dto.request.CreateOrderRequest;
import com.akhil.orders.dto.response.OrderResponse;
import com.akhil.orders.mapper.OrderMapper;
import com.akhil.orders.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public OrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) throws JsonProcessingException {

        Order order = orderService.createOrder(request);

        return OrderMapper.toResponse(order);
    }

    @PostMapping("/{orderId}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse confirmOrder(@PathVariable UUID orderId) {
        return OrderMapper.toResponse(
                orderService.confirmOrder(orderId)
        );
    }

    @PostMapping("/{orderId}/pay")
    public OrderResponse markPaid(@PathVariable UUID orderId) {
        return OrderMapper.toResponse(
                orderService.markOrderPaid(orderId)
        );
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public OrderResponse cancel(@PathVariable UUID orderId) {
        return OrderMapper.toResponse(
                orderService.cancelOrder(orderId)
        );
    }
}
