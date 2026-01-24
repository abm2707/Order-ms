package com.akhil.orders.Exceptions;

import com.akhil.orders.dto.response.InventoryErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ExceptionHandler(InventoryProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public InventoryErrorResponse handleInventoryNotFound(
            InventoryProductNotFoundException ex) {

        return new InventoryErrorResponse(
                ex.getCode(),
                ex.getMessage()
        );
    }
}

