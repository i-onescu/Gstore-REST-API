package com.gstore.gstoreapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gstore.gstoreapi.controllers.util.ResponseBuilderHelper;
import com.gstore.gstoreapi.enums.ProductCategory;
import com.gstore.gstoreapi.exceptions.InvalidPayloadException;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ResponsePayload> saveProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return ResponseBuilderHelper.buildResponsePayload("Product posted!",
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<ResponsePayload> getAllProducts() {
        return ResponseBuilderHelper.buildResponsePayload(productService.getAllProducts(),
                HttpStatus.OK);
    }


    @GetMapping("/category")
    public ResponseEntity<ResponsePayload> getAllProductsByCategory(@RequestParam String name) {
        try {
            return ResponseBuilderHelper.buildResponsePayload(productService.getAllProductsByCategory(name),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseBuilderHelper.buildResponsePayload("No such category exists!",
                    HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getProductById(@PathVariable("id") Long id) {
        return ResponseBuilderHelper.buildResponsePayload(productService.getProductById(id),
                HttpStatus.OK);
    }


    @GetMapping("/salesFor")
    public ResponseEntity<ResponsePayload> getProductSales(@RequestParam Long id) {
        return ResponseBuilderHelper.buildResponsePayload(productService.getProductSales(id),
                HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateProductDetails(@PathVariable("id") Long id,
                                                                @RequestBody ProductDTO productDTO) {
        productService.updateProductDetails(id, productDTO);
        return ResponseBuilderHelper.buildResponsePayload("Updated product!",
                HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProductById(id);
        return ResponseBuilderHelper.buildResponsePayload(String.format("Deleted product with id %d!", id),
                HttpStatus.NO_CONTENT);
    }


    @PutMapping("/rate")
    public ResponseEntity<ResponsePayload> rateProduct(@RequestBody ProductDTO productDTO,
                                                       HttpServletRequest request) {
        try {
            Long sessionId = Long.valueOf(request.getHeader("Session-Id"));
            productService.rate(sessionId, productDTO);
            return getProductById(productDTO.Id());
        } catch (InvalidPayloadException e) {
            return ResponseBuilderHelper.buildResponsePayload("Bad request!",
                    HttpStatus.BAD_REQUEST);
        }
    }




    @GetMapping("/categories")
    public ResponseEntity<ResponsePayload> getProductCategories() {
        return ResponseBuilderHelper.buildResponsePayload(
                String.format("GAMING " +
                        "    TECH " +
                        "    SOFTWARE " +
                        "    HOUSEHOLD " +
                        "    FOODSTUFFS " +
                        "    SPORTS"),
                HttpStatus.OK);
    }





}
