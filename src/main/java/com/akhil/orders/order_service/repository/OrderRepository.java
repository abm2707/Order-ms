package com.akhil.orders.order_service.repository;

import com.akhil.orders.order_service.domain.entity.Order;
import com.akhil.orders.order_service.domain.valueobject.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    long countByStatus(OrderStatus status);
}
