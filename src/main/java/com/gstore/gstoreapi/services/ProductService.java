package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectConverter<Product, ProductDTO> productConverter;


    //returns all products
    public List<ProductDTO> getAllProducts() {
        //list of all entities found in repository
        List<Product> products = productRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return products.stream()
                .map(productConverter::convertFirstToSecond)
                .toList();
    }

    //method to get products from repository by its id
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(ProductNotFoundException::new);

        return productConverter.convertFirstToSecond(product);
    }

    //method to delete product by its id
    public void deleteProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }

    //method to save products
    public void saveProduct(@Valid ProductDTO productDTO) {
        //converting ProductDTO object to Product
        Product product = productConverter.convertSecondToFirst(productDTO);

        //saving the product
        productRepository.save(product);
    }

    //updates a specific product based on an id and DTO containing new details
    public void updateProductDetails(Long id, ProductDTO productDTO) {
        if (productRepository.findProductById(id).isPresent()) {
            Product product = productRepository.findProductById(id).get();
            Product patchProduct = productConverter.convertSecondToFirst(productDTO);

            updateProduct(product, patchProduct);
            productRepository.save(product);
        } else throw new ProductNotFoundException();
    }

    public void updateProduct(Product product, Product patchProduct) {

        if (patchProduct.getName() != null) {
            product.setName(patchProduct.getName());
        }

        if (patchProduct.getManufacturer() != null) {
            product.setManufacturer(patchProduct.getManufacturer());
        }

        if (patchProduct.getCountry() != null) {
            product.setCountry(patchProduct.getCountry());
        }

        if (patchProduct.getPrice() != null) {
            product.setPrice(patchProduct.getPrice());
        }

        if (patchProduct.getRating() != null) {
            product.setRating(patchProduct.getRating());
        }
    }
}