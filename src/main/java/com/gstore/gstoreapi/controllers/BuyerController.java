package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilder;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.BuyerService;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyers")
public class BuyerController {

    private final BuyerService buyerService;

    public BuyerController(BuyerService buyerService) {
        this.buyerService = buyerService;
    }


    @GetMapping
    public ResponseEntity<ResponsePayload> getAllBuyers() {
        return ResponseBuilder.buildResponsePayload(buyerService.getAllBuyers(),
                HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getBuyerById(@PathVariable("id") Long id) {
        try {
            return ResponseBuilder.buildResponsePayload(buyerService.getBuyerById(id),
                    HttpStatus.FOUND);
        } catch (BuyerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Buyer with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ResponsePayload> saveBuyer(@RequestBody BuyerDTO buyerDTO) {
        try {
            buyerService.saveBuyer(buyerDTO);
            return ResponseBuilder.buildResponsePayload("Buyer created!",
                    HttpStatus.CREATED);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Bad request!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteBuyer(@PathVariable Long id) {
        try {
            buyerService.deleteBuyerById(id);
            return ResponseBuilder.buildResponsePayload("Successfully deleted buyer!",
                    HttpStatus.OK);
        } catch (BuyerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Buyer with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateBuyerDetails(@PathVariable("id") Long id,
                                                              @RequestBody BuyerDTO buyerDTO) {
        try {
            buyerService.updateBuyerDetails(id, buyerDTO);
            return ResponseBuilder.buildResponsePayload("Buyer Updated!",
                    HttpStatus.OK);
        } catch (ValidationException e){
            return ResponseBuilder.buildResponsePayload(String.format("Cannot update buyer with id %d!", id),
                    HttpStatus.BAD_REQUEST);
        }
    }
}



