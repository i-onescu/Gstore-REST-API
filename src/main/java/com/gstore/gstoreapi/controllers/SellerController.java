package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilderHelper;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.models.dtos.SellerDTO;
import com.gstore.gstoreapi.services.SellerService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<ResponsePayload> saveSeller(@RequestBody SellerDTO sellerDTO) {
        sellerService.saveSeller(sellerDTO);
        return ResponseBuilderHelper.buildResponsePayload("Seller created!",
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<ResponsePayload> getAllSellers() {
        return ResponseBuilderHelper.buildResponsePayload(sellerService.getAllSellers(),
                HttpStatus.FOUND);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getSellerById(@PathVariable("id") Long id) {
        return ResponseBuilderHelper.buildResponsePayload(sellerService.getSellerById(id),
                HttpStatus.FOUND);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateSellerDetails(@PathVariable("id") Long id,
                                                               @RequestBody SellerDTO sellerDTO) {
        sellerService.updateSellerDetails(id, sellerDTO);
        return ResponseBuilderHelper.buildResponsePayload("Seller Updated!",
                HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSellerById(id);
        return ResponseBuilderHelper.buildResponsePayload("Successfully deleted seller!",
                HttpStatus.NO_CONTENT);
    }

}

