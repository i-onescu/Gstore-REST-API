package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilder;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.exceptions.OrderNotFoundException;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.OrderService;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OderController {

    public final OrderService orderService;

    public OderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @GetMapping
    public ResponseEntity<ResponsePayload> getAllOrders() {
        return ResponseBuilder.buildResponsePayload(orderService.getAllOrders(),
                HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getOrderById(@PathVariable Long id) {
        try {
            return ResponseBuilder.buildResponsePayload(orderService.getOrderById(id),
                    HttpStatus.FOUND);
        } catch (OrderNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Order with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteOrderById(@PathVariable Long id) {
        try {
            orderService.deleteOrderById(id);

            return ResponseBuilder.buildResponsePayload(String.format("Deleted order with id %d.", id),
                    HttpStatus.NO_CONTENT);
        } catch (OrderNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Order with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/place")
    public ResponseEntity<ResponsePayload> placeOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.placeOrder(orderDTO);
            return ResponseBuilder.buildResponsePayload("Order placed!",
                    HttpStatus.CREATED);
        } catch (ProductNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("One of the products listed was incorrect!",
                    HttpStatus.BAD_REQUEST);
        } catch (BuyerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("Buyer ID invalid!",
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Wrong request!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateOrderDetails(@PathVariable("id") Long id,
                                                              @RequestBody OrderDTO orderDTO) {
        try {
            orderService.updateOrderDetails(id, orderDTO);
            return ResponseBuilder.buildResponsePayload("Updated order!",
                    HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("One of the products ids listed was incorrect!",
                    HttpStatus.BAD_REQUEST);
        } catch (BuyerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Buyer with id %d does not exist", orderDTO.buyerId()),
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Bad request!",
                    HttpStatus.BAD_REQUEST);
        }
    }

}
