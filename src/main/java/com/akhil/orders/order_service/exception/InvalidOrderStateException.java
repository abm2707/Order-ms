package com.akhil.orders.order_service.exception;

public class InvalidOrderStateException extends RuntimeException{
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
