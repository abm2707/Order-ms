package com.akhil.orders.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal mrp;

    @Column(nullable = false)
    private BigDecimal rate;

    @Column
    private BigDecimal discount;

    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    public OrderItem(
            UUID productId,
            int quantity,
            BigDecimal mrp,
            BigDecimal rate,
            BigDecimal discount,
            BigDecimal finalPrice,
            Order order
    ) {
        this.productId = productId;
        this.quantity = quantity;
        this.mrp = mrp;
        this.rate = rate;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.order = order;
    }
}

