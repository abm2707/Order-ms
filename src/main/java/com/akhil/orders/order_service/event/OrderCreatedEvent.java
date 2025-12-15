package com.akhil.orders.order_service.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCreatedEvent {

    @JsonProperty("orderId")
    private UUID orderId;
    
    @JsonProperty("customerId")
    private UUID customerId;
    
    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("createdAt")
    private Instant createdAt;

    @JsonCreator
    public OrderCreatedEvent(@JsonProperty("orderId") UUID orderId,
                            @JsonProperty("customerId") UUID customerId,
                            @JsonProperty("totalAmount") BigDecimal totalAmount,
                            @JsonProperty("currency") String currency,
                            @JsonProperty("createdAt") Instant createdAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }
}