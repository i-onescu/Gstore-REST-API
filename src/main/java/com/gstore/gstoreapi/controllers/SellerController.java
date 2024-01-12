package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilder;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.models.dtos.SellerDTO;
import com.gstore.gstoreapi.services.SellerService;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }



    @GetMapping
    public ResponseEntity<ResponsePayload> getAllSellers() {
        return ResponseBuilder.buildResponsePayload(sellerService.getAllSellers(),
                HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getSellerById(@PathVariable("id") Long id) {
        try {
            return ResponseBuilder.buildResponsePayload(sellerService.getSellerById(id),
                    HttpStatus.FOUND);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Seller with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteSeller(@PathVariable Long id) {
        try {
            sellerService.deleteSellerById(id);
            return ResponseBuilder.buildResponsePayload("Successfully deleted seller!",
                    HttpStatus.OK);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Seller with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ResponsePayload> saveSeller(@RequestBody SellerDTO sellerDTO) {
        try {
            sellerService.saveSeller(sellerDTO);
            return ResponseBuilder.buildResponsePayload("Seller created!",
                    HttpStatus.CREATED);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Bad request!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateSellerDetails(@PathVariable("id") Long id,
                                                               @RequestBody SellerDTO sellerDTO) {
        try {
            return sellerService.updateSellerDetails(id, sellerDTO)
                    ? ResponseBuilder.buildResponsePayload("Seller Updated!",
                    HttpStatus.OK)
                    : ResponseBuilder.buildResponsePayload("Seller Created!",
                    HttpStatus.CREATED);
        } catch (ValidationException e){
            return ResponseBuilder.buildResponsePayload(String.format("Cannot update seller with id %d!", id),
                    HttpStatus.BAD_REQUEST);
        }
    }
}

