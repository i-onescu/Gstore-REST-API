package com.gstore.gstoreapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gstore.gstoreapi.controllers.util.ResponseBuilderHelper;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.BuyerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;


    @PostMapping
    public ResponseEntity<ResponsePayload> saveBuyer(@RequestBody BuyerDTO buyerDTO) {
        buyerService.saveBuyer(buyerDTO);
        return ResponseBuilderHelper.buildResponsePayload("Buyer created!",
                HttpStatus.CREATED);

    }


    @GetMapping
    public ResponseEntity<ResponsePayload> getAllBuyers() {
        return ResponseBuilderHelper.buildResponsePayload(buyerService.getAllBuyers(),
                HttpStatus.FOUND);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getBuyerById(@PathVariable("id") Long id) {
        return ResponseBuilderHelper.buildResponsePayload(buyerService.getBuyerById(id),
                HttpStatus.FOUND);
    }


    @GetMapping("/orders")
    public ResponseEntity<ResponsePayload> getBuyerOrders(HttpServletRequest request) {
        Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
        return ResponseBuilderHelper.buildResponsePayload(buyerService.getBuyerOrders(sessionId),
                HttpStatus.FOUND);
    }


    @PutMapping("/edit")
    public ResponseEntity<ResponsePayload> updateBuyerDetails(HttpServletRequest request,
                                                              @RequestBody BuyerDTO buyerDTO) {

        Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
        buyerService.updateBuyerDetails(sessionId, buyerDTO);
        return ResponseBuilderHelper.buildResponsePayload("Buyer Updated!",
                HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteBuyer(@PathVariable Long id) {
        buyerService.deleteBuyerById(id);
        return ResponseBuilderHelper.buildResponsePayload("Successfully deleted buyer!",
                HttpStatus.OK);
    }


}



