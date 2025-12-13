package com.akhil.orders.order_service.domain.entity;

import com.akhil.orders.order_service.domain.valueobject.OrderStatus;
import com.akhil.orders.order_service.exception.InvalidOrderStateException;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_order_order_number", columnNames = "order_number")
        }
)
public class Order {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "order_number", nullable = false, updatable = false)
    private String orderNumber;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    protected Order() {
        // JPA
    }

    public Order(String orderNumber,
                 UUID customerId,
                 BigDecimal totalAmount,
                 String currency) {

        this.id = UUID.randomUUID();
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.status = OrderStatus.CREATED;
    }

    /* ======================
       Domain Behavior
       ====================== */

    public void confirm() {
        if (this.status != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    "Order can only be confirmed from CREATED state"
            );
        }
        this.status = OrderStatus.CONFIRMED;
    }

    public void markPaid() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException(
                    "Order can only be paid after it is CONFIRMED"
            );
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (this.status == OrderStatus.SHIPPED) {
            throw new InvalidOrderStateException(
                    "Shipped orders cannot be cancelled"
            );
        }
        if (this.status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(
                    "Order is already cancelled"
            );
        }
        this.status = OrderStatus.CANCELLED;
    }

    /* ======================
       Audit Hooks
       ====================== */

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}