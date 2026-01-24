package com.akhil.orders.application.query;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.dto.response.OrderItemResponse;
import com.akhil.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderItemResponse> getOrderItems(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        return order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getMrp(),
                        item.getDiscount(),
                        item.getFinalPrice(),
                        item.getRate()
                ))
                .toList();
    }
}

