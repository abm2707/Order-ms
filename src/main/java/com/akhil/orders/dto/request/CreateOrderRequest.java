package com.akhil.orders.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderRequest {

    @NotNull
    private UUID customerId;

    @NotNull
    private BigDecimal totalAmount;

    @NotBlank
    private String currency;

    @NotEmpty
    private List<OrderItemRequest> items;
}
