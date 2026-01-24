package com.akhil.orders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryErrorResponse {
    private String code;
    private String message;
}

