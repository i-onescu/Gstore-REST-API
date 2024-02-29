package com.gstore.gstoreapi.exceptions;

public class SellerNotFoundException extends RuntimeException {

    private Long sellerId;

    public SellerNotFoundException() {
    }

    public SellerNotFoundException(Long sellerId) {
        this.sellerId = sellerId;
    }

    public SellerNotFoundException(String message) {
        super(message);
    }

    public SellerNotFoundException(String message, Long sellerId) {
        super(message);
        this.sellerId = sellerId;
    }

    public SellerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SellerNotFoundException(Throwable cause) {
        super(cause);
    }

    public SellerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Long getSellerId() {
        return sellerId;
    }
}

