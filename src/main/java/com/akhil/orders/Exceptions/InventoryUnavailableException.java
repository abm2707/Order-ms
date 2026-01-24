package com.akhil.orders.Exceptions;

import java.util.UUID;

public class InventoryUnavailableException extends RuntimeException {

    private final String code;

    public InventoryUnavailableException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
