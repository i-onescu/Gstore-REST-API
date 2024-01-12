package com.gstore.gstoreapi.controllers;

import com.gstore.gstoreapi.controllers.util.ResponseBuilder;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.dtos.ResponsePayload;
import com.gstore.gstoreapi.services.ProductService;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    @GetMapping
    public ResponseEntity<ResponsePayload> getAllProducts() {
        return ResponseBuilder.buildResponsePayload(productService.getAllProducts(),
                HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePayload> getProductById(@PathVariable("id") Long id) {
        try {
            return ResponseBuilder.buildResponsePayload(productService.getProductById(id),
                    HttpStatus.FOUND);
        } catch (ProductNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Product with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponsePayload> deleteProductById(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseBuilder.buildResponsePayload(String.format("Deleted product with id %d!", id),
                    HttpStatus.NO_CONTENT);
        } catch (ProductNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Product with id %d not found!", id),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ResponsePayload> saveProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.saveProduct(productDTO);
            return ResponseBuilder.buildResponsePayload("Product posted!",
                    HttpStatus.CREATED);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("Seller ID invalid!",
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Wrong request!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateProductDetails(@PathVariable("id") Long id,
                                                                @RequestBody ProductDTO productDTO) {
        try {
            productService.updateProductDetails(id ,productDTO);
            return ResponseBuilder.buildResponsePayload("Updated product!",
                    HttpStatus.OK);
        }  catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Seller with id %d does not exist", id),
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Something else wrong!",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
