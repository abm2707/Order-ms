package com.akhil.orders.dto.request;

import com.akhil.orders.domain.valueobject.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
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

    @NotNull
    private PaymentMethod paymentMethod;

    @NotEmpty
    private List<OrderItemRequest> items;
}
