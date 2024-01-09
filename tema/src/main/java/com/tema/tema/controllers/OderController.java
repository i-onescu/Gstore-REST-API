package com.tema.tema.controllers;

import com.tema.tema.controllers.util.ResponseBuilder;
import com.tema.tema.exceptions.SellerNotFoundException;
import com.tema.tema.models.dtos.OrderDTO;
import com.tema.tema.models.dtos.ResponsePayload;
import com.tema.tema.services.OrderService;
import com.tema.tema.exceptions.BuyerNotFoundException;
import com.tema.tema.exceptions.OrderNotFoundException;
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
    public ResponseEntity<ResponsePayload> getOrderById(@PathVariable("id") Long id) {
        try {
            return ResponseBuilder.buildResponsePayload(orderService.getOrderById(id),
                    HttpStatus.FOUND);
        } catch (OrderNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Order with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteOrderById(@PathVariable("id") Long id) {
        try {
            orderService.deleteOrderById(id);

            return ResponseBuilder.buildResponsePayload(String.format("Deleted order with id %d.", id),
                    HttpStatus.NO_CONTENT);
        } catch (OrderNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Order with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ResponsePayload> saveOrder(@RequestBody OrderDTO orderDTO) {
        try {
            orderService.saveOrder(orderDTO);
            return ResponseBuilder.buildResponsePayload("Order saved!",
                    HttpStatus.CREATED);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Wrong request!",
                    HttpStatus.BAD_REQUEST);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("Seller ID invalid!",
                    HttpStatus.BAD_REQUEST);
        } catch (BuyerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("Buyer ID invalid!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateOrderDetails(@PathVariable("id") Long id,
                                                                @RequestBody OrderDTO orderDTO) {
        try {
            return orderService.updateOrderDetails(id, orderDTO)
                    ? ResponseBuilder.buildResponsePayload("Order updated!",
                    HttpStatus.OK)
                    : ResponseBuilder.buildResponsePayload("Order updated!",
                    HttpStatus.CREATED);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Seller with id %d does not exist", id),
                    HttpStatus.BAD_REQUEST);
        } catch (BuyerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Buyer with id %d does not exist", id),
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Something else wrong!",
                    HttpStatus.BAD_REQUEST);
        }
    }






}
