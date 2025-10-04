package com.ecommerce_micro_service.user.exceptions;

public class APIException extends RuntimeException {
    public APIException() {
    }
    public APIException(String message) {
        super(message);
    }
}
