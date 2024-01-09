package com.tema.tema.controllers;

import com.tema.tema.controllers.util.ResponseBuilder;
import com.tema.tema.exceptions.ProductNotFoundException;
import com.tema.tema.exceptions.SellerNotFoundException;
import com.tema.tema.models.dtos.ProductDTO;
import com.tema.tema.models.dtos.ResponsePayload;
import com.tema.tema.services.ProductService;
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

            return ResponseBuilder.buildResponsePayload(String.format("Deleted product with id %d.", id),
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
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Wrong request!",
                    HttpStatus.BAD_REQUEST);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload("Seller ID invalid!",
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePayload> updateProductDetails(@PathVariable("id") Long id,
                                                                @RequestBody ProductDTO productDTO) {
        try {
            return productService.updateProductDetails(id, productDTO)
                    ? ResponseBuilder.buildResponsePayload("User updated!",
                    HttpStatus.OK)
                    : ResponseBuilder.buildResponsePayload("User updated!",
                    HttpStatus.CREATED);
        } catch (SellerNotFoundException e) {
            return ResponseBuilder.buildResponsePayload(String.format("Seller with id %d does not exist", id),
                    HttpStatus.BAD_REQUEST);
        } catch (ValidationException e) {
            return ResponseBuilder.buildResponsePayload("Something else wrong!",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
