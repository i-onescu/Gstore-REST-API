package com.gstore.gstoreapi.exceptions;

public class ProductNotFoundException extends RuntimeException {

    private Long productId;

    public ProductNotFoundException() {
    }

    public ProductNotFoundException(Long productId) {
        this.productId = productId;
    }

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Long productId) {
        super(message);
        this.productId = productId;
    }

    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProductNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public Long getProductId() {
        return productId;
    }
}

