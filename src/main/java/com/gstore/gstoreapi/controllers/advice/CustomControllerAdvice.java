package com.gstore.gstoreapi.controllers.advice;

import com.gstore.gstoreapi.controllers.util.ResponseBuilderHelper;
import com.gstore.gstoreapi.exceptions.*;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler({
            BuyerNotFoundException.class,
            SellerNotFoundException.class,
            ProductNotFoundException.class,
            OrderNotFoundException.class,
            OrderNotModifiableException.class
    })
    public ResponseEntity<ResponsePayload> handleNotFound(RuntimeException e, HttpServletRequest request) {

        /*
         MAKE THIS METHOD HANDLE ALL POSSIBILITIES FOR ALL TYPES OF METHODS
         BY USING THE request.getMethod() to see what type of http error to return
         */

        if (e instanceof BuyerNotFoundException exception && request.getMethod().equals("POST")) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Bad request: Buyer with id %d does not exist!",
                            exception.getBuyerId()),
                    HttpStatus.BAD_REQUEST);
        } else if (e instanceof BuyerNotFoundException exception) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Buyer with id %d not found!",
                            exception.getBuyerId()),
                    HttpStatus.NOT_FOUND);
        } else if (e instanceof SellerNotFoundException exception && request.getMethod().equals("POST")) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Bad request: Seller with id %d does not exist!",
                            exception.getSellerId()),
                    HttpStatus.BAD_REQUEST);
        } else if (e instanceof SellerNotFoundException exception) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Seller with id %d not found!",
                            exception.getSellerId()),
                    HttpStatus.NOT_FOUND);
        } else if (e instanceof ProductNotFoundException exception && request.getMethod().equals("POST")) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Bad request: Product with id %d does not exist!",
                            exception.getProductId()),
                    HttpStatus.BAD_REQUEST);
        } else if (e instanceof ProductNotFoundException exception) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Product with id %d not found!",
                            exception.getProductId()),
                    HttpStatus.NOT_FOUND);
        } else if (e instanceof OrderNotFoundException exception) {
            return ResponseBuilderHelper.buildResponsePayload(String.format("Order with id %d not found!",
                            exception.getOrderId()),
                    HttpStatus.NOT_FOUND);
        } else {
            return ResponseBuilderHelper.buildResponsePayload("Order not modifiable!",
                    HttpStatus.CONFLICT);
        }
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponsePayload> handleValidationException(ValidationException e) {
        return ResponseBuilderHelper.buildResponsePayload("Wrong request!",
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({InvalidSessionIdException.class, ExistingActiveSessionException.class, NumberFormatException.class})
    public ResponseEntity<ResponsePayload> handleSessionExceptions(Exception e) {
        if (e instanceof InvalidSessionIdException exception) {
            return ResponseBuilderHelper.buildResponsePayload(exception.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } else if(e instanceof ExistingActiveSessionException exception){
            return ResponseBuilderHelper.buildResponsePayload(exception.getMessage(),
                    HttpStatus.CONFLICT);
        } else {
            return ResponseBuilderHelper.buildResponsePayload("Please provide session ID!",
                    HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler({EmptyCartException.class})
    public ResponseEntity<ResponsePayload> handleCartExceptions() {
        return ResponseBuilderHelper.buildResponsePayload("Your cart is currently empty.",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EmailTakenException.class})
    public ResponseEntity<ResponsePayload> handleEmailException() {
        return ResponseBuilderHelper.buildResponsePayload("Email is already registered to another user!",
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IncorrectLoginDetailsException.class})
    public ResponseEntity<ResponsePayload> handleLoginException(Exception e) {
        return ResponseBuilderHelper.buildResponsePayload(e.getMessage(),
                HttpStatus.CONFLICT);
    }



}
