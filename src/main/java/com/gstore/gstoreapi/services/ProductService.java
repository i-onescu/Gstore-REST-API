package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.converters.impl.ProductConverter;
import com.gstore.gstoreapi.enums.ProductCategory;
import com.gstore.gstoreapi.exceptions.InvalidPayloadException;
import com.gstore.gstoreapi.exceptions.InvalidSessionIdException;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.entities.CustomSession;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.ProductRepository;
import com.gstore.gstoreapi.repositories.QuantityRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final QuantityRepository quantityRepository;
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final CustomSessionService customSessionService;


    //saves a new product based on a provided DTO
    public void saveProduct(@Valid ProductDTO productDTO) {
        //converts DTO to an instance of Product
        Product product = productConverter.convertSecondToFirst(productDTO);

        //verifying if seller provided in ProductDTO is valid and throwing exceptions based on results
        if (product.getSeller() == null && productDTO.sellerId() != null) {
            throw new SellerNotFoundException(productDTO.sellerId());
        } else if (product.getSeller() == null) {
            throw new ValidationException("Seller cannot be null!");
        }

        //rating is set to null, rating is only changed when product is rated by customer
        product.setRating(null);

        //new product is saved
        productRepository.save(product);
    }


    //retrieves all products
    public List<ProductDTO> getAllProducts() {
        //list of all entities found in repository
        List<Product> products = productRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return products.stream()
                .map(productConverter::convertFirstToSecond)
                .toList();
    }


    //retrieves all products pertaining to a specific category
    public List<ProductDTO> getAllProductsByCategory(String name) throws IllegalArgumentException{
        //list of all entities found in repository
        List<Product> products = productRepository.findAllByCategory(ProductCategory.valueOf(name));

        //stream converts entities into DTOs and maps into a list
        return products.stream()
                .map(productConverter::convertFirstToSecond)
                .toList();
    }


    //retrieves a product from repository based on provided id
    public ProductDTO getProductById(Long id) {
        //throws exception if a product with the provided id is not found
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        //returns a converted DTO
        return productConverter.convertFirstToSecond(product);
    }


    //retrieves the total sales of a particular product
    public Map<String, Object> getProductSales(Long id) {
        //retrieves product
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        //calculates sales volume from all quantities from database containing product
        Double salesVolume = quantityRepository.findQuantitiesByProduct(product).stream()
                .mapToDouble(Quantity::getQuantity)
                .sum();

        //creates a map with a field for the product name and one for the total sales volume previously calculated
        Map<String, Object> salesPretty = new HashMap<>();
        salesPretty.put("totalSalesVolume", salesVolume);
        salesPretty.put("productName", product.getName());

        //map is then returned to the controller to be parsed as json to client
        return salesPretty;
    }


    //updates a product from repository based on an id and DTO containing new details (both provided by client)
    public void updateProductDetails(Long id, ProductDTO productDTO) {
        //firstly, does a quick verification to see if a product with the provided id is found
        //if not, throws exception thus saving time
        if (productRepository.findProductById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        //retrieves the product to be updated and instantiates a new product with details from the provided DTO
        Product product = productRepository.findProductById(id).get();
        Product patchProduct = productConverter.convertSecondToFirst(productDTO);

        //updates product with new details and saves changes in persistence layer
        updateProduct(product, patchProduct);
        productRepository.save(product);
    }


    //updates product details
    //only updates what is allowed, does not change id
    public void updateProduct(Product product, Product patchProduct) {

        //not null validation was put in place because null/missing fields are allowed in payload,
        //as the client must provide only the fields and values that they wish to update for the chosen product

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

        if (patchProduct.getCategory() != null) {
            product.setCategory(patchProduct.getCategory());
        }
    }


    //deletes product from repository based on provided id
    public void deleteProductById(Long id) {
        //throws exception if a product with the provided id is not found
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        //deletes product
        productRepository.delete(product);
    }


    public void rate(Long sessionId, ProductDTO productDTO) {

        if(productDTO.rating() == null || productDTO.Id() == null){
            throw new InvalidPayloadException();
        }

        if (sessionId == null) {
            throw new InvalidSessionIdException("Please provide a session id!");
        }

        Optional<CustomSession> sessionById = customSessionService.getCustomSessionById(sessionId);
        if (sessionById.isEmpty()) {
            throw new InvalidSessionIdException("Invalid session id!");
        }

        Product product = productRepository.findProductById(productDTO.Id())
                .orElseThrow(() -> new ProductNotFoundException(String.format("No product with id %d found!",
                        productDTO.Id())));

        if (product.getRating() == null) {
            product.setRating(productDTO.rating());
        } else {
            product.setRating((product.getRating() + productDTO.rating()) / 2);
        }

    }
}