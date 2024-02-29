package com.gstore.gstoreapi.exceptions;

import org.springframework.http.HttpStatus;

public class BuyerNotFoundException extends RuntimeException {

    private Long buyerId;

    private HttpStatus status;

    public BuyerNotFoundException() {
    }

    public BuyerNotFoundException(Long buyerId) {
        this.buyerId = buyerId;
    }

    public BuyerNotFoundException(String message) {
        super(message);
    }

    public BuyerNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BuyerNotFoundException(String message, Long buyerId) {
        super(message);
        this.buyerId = buyerId;
    }

    public BuyerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuyerNotFoundException(Throwable cause) {
        super(cause);
    }

    public BuyerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Long getBuyerId() {
        return buyerId;
    }
}