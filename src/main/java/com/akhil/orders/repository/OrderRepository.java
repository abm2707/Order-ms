package com.akhil.orders.repository;

import com.akhil.orders.domain.entity.Order;
import com.akhil.orders.domain.valueobject.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNumber(String orderNumber);

    boolean existsByOrderNumber(String orderNumber);

    long countByStatus(OrderStatus status);

    @Modifying
    @Query("""
    UPDATE Order o
    SET o.status = :newStatus
    WHERE o.id = :orderId
      AND o.status = :currentStatus
""")
    int updateStatusIfCurrent(
            UUID orderId,
            OrderStatus currentStatus,
            OrderStatus newStatus
    );

}

