package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilderHelper;
import com.gstore.gstoreapi.models.dtos.QuantityDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.BuyerService;
import com.gstore.gstoreapi.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final BuyerService buyerService;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponsePayload> goToCart(HttpServletRequest request) {
        Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
        return ResponseBuilderHelper.buildResponsePayload(
                buyerService.goToCart(sessionId),
                HttpStatus.OK);
    }


    @PutMapping("/add")
    public ResponseEntity<ResponsePayload> addProductToCart(@RequestBody QuantityDTO quantityDTO,
                                                            HttpServletRequest request) {
        Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
        buyerService.addProductToCart(sessionId, quantityDTO);

        return ResponseBuilderHelper.buildResponsePayload(
                String.format("Added %d items with id %d to cart!", quantityDTO.quantity(), quantityDTO.productId()),
                HttpStatus.OK
        );
    }


    @PutMapping("/remove")
    public ResponseEntity<ResponsePayload> removeProductFromCart(@RequestBody QuantityDTO quantityDTO,
                                                                 HttpServletRequest request) {
        Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
        buyerService.removeProductFromCart(sessionId, quantityDTO);

        return ResponseBuilderHelper.buildResponsePayload(
                String.format("Removed %d items with id %d from cart!", quantityDTO.quantity(), quantityDTO.productId()),
                HttpStatus.OK
        );
    }


    @PostMapping("/checkout")
    public ResponseEntity<ResponsePayload> goToCheckout(HttpServletRequest request) {
        Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
        orderService.placeOrder(buyerService.getCartCheckout(sessionId));
        return ResponseBuilderHelper.buildResponsePayload("Placed order!", HttpStatus.OK);
    }

}
