package com.tema.tema.controllers;

import com.tema.tema.controllers.util.ResponseBuilder;
import com.tema.tema.models.dtos.ResponsePayload;
import com.tema.tema.services.BuyerService;
import com.tema.tema.exceptions.BuyerNotFoundException;
import com.tema.tema.models.dtos.BuyerDTO;
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
            return buyerService.updateBuyerDetails(id, buyerDTO)
                    ? ResponseBuilder.buildResponsePayload("Buyer Updated!",
                    HttpStatus.OK)
                    : ResponseBuilder.buildResponsePayload("Buyer Updated!",
                    HttpStatus.CREATED);
        } catch (ValidationException e){
            return ResponseBuilder.buildResponsePayload(String.format("Cannot update buyer with id %d!", id),
                    HttpStatus.BAD_REQUEST);
        }
    }
}



















