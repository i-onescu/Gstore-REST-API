package com.gstore.gstoreapi.exceptions;

public class OrderNotFoundException extends RuntimeException {

    private Long orderId;

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(Long orderId) {
        this.orderId = orderId;
    }

    public OrderNotFoundException(String message, Long orderId) {
        super(message);
        this.orderId = orderId;
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super(cause);
    }

    public OrderNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Long getOrderId() {
        return orderId;
    }
}

