package com.ecommerce_micro_service.order.exceptions;

public class APIException extends RuntimeException {
    public APIException() {
    }
    public APIException(String message) {
        super(message);
    }
}
