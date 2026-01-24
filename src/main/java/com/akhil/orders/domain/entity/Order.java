package com.akhil.orders.domain.entity;

import com.akhil.orders.domain.valueobject.OrderStatus;
import com.akhil.orders.exception.InvalidOrderStateException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(
        name = "orders",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_order_number",
                        columnNames = "order_number"
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    /* ======================
       Optimistic Locking
       ====================== */

    @Version
    private Long version;

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

    // Methods to add items to order.
    public void addItem(
            UUID productId,
            int quantity,
            BigDecimal mrp,
            BigDecimal rate,
            BigDecimal discount,
            BigDecimal finalPrice
    ) {
        this.items.add(
                new OrderItem(
                        productId,
                        quantity,
                        mrp,
                        rate,
                        discount,
                        finalPrice,
                        this
                )
        );
    }

    /* ======================
       Constructor
       ====================== */

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
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
                    "Order can only be paid after CONFIRMED state"
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
       JPA Lifecycle Hooks
       ====================== */

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }
}
