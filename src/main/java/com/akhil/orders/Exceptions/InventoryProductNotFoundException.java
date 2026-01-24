package com.akhil.orders.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class InventoryProductNotFoundException extends RuntimeException {

    private final String code;

    public InventoryProductNotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}
