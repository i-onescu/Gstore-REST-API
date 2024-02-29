package com.gstore.gstoreapi.exceptions;

public class ProductNotAvailableException extends RuntimeException {

    private Long productId;

    public ProductNotAvailableException() {
    }

    public ProductNotAvailableException(Long productId) {
        this.productId = productId;
    }

    public ProductNotAvailableException(String message, Long productId) {
        super(message);
        this.productId = productId;
    }

    public ProductNotAvailableException(String message) {
        super(message);
    }

    public ProductNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotAvailableException(Throwable cause) {
        super(cause);
    }

    public ProductNotAvailableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
