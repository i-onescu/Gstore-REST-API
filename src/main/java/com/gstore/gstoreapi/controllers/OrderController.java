package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilderHelper;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    public final OrderService orderService;


    //CREATE
    @PostMapping("/place")
    public ResponseEntity<ResponsePayload> placeOrder(@RequestBody OrderDTO orderDTO, HttpServletRequest request) {
        orderService.placeOrder(orderDTO);
        return ResponseBuilderHelper.buildResponsePayload("Order placed!",
                HttpStatus.CREATED);
    }


    //READ
    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getOrderById(@PathVariable Long id) {
        return ResponseBuilderHelper.buildResponsePayload(orderService.getOrderById(id),
                HttpStatus.FOUND);
    }


    //UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateOrderDetails(@PathVariable("id") Long id,
                                                              @RequestBody OrderDTO orderDTO) {
        orderService.updateOrderDetails(id, orderDTO);
        return ResponseBuilderHelper.buildResponsePayload("Updated order!",
                HttpStatus.OK);
    }


    //DELETE
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ResponsePayload> cancelOrderById(@PathVariable Long id) {
        return orderService.cancelOrderById(id)
                ? ResponseBuilderHelper.buildResponsePayload("Order already canceled!",
                HttpStatus.CONFLICT)
                : ResponseBuilderHelper.buildResponsePayload("Canceled order!",
                HttpStatus.OK);
    }


}
