package com.akhil.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemResponse {

    private UUID productId;
    private int quantity;
    private BigDecimal mrp;
    private BigDecimal rate;
    private BigDecimal discount;
    private BigDecimal finalPrice;

    public OrderItemResponse(
            UUID productId,
            int quantity,
            BigDecimal mrp,
            BigDecimal rate,
            BigDecimal discount,
            BigDecimal finalPrice
    ) {
        this.productId = productId;
        this.quantity = quantity;
        this.mrp = mrp;
        this.rate = rate;
        this.discount = discount;
        this.finalPrice = finalPrice;
    }

}
