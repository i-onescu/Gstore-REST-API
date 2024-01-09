package com.tema.tema.services;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.exceptions.ProductNotFoundException;
import com.tema.tema.exceptions.SellerNotFoundException;
import com.tema.tema.repositories.ProductRepository;
import com.tema.tema.repositories.SellerRepository;
import com.tema.tema.models.dtos.ProductDTO;
import com.tema.tema.models.entities.Product;
import com.tema.tema.models.entities.Seller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    //injecting a sellerRepository in order to parse a seller
    //in the seller field when converting DTO to entity
    // during creation of new product
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ObjectConverter<Product, ProductDTO> productConverter;


    public ProductService (ProductRepository productRepository,
                           SellerRepository sellerRepository,
                           ObjectConverter<Product, ProductDTO> productConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.sellerRepository = sellerRepository;
    }


    public List<ProductDTO> getAllProducts(){
        //list of all entities found in repository
        List<Product> products = productRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return products.stream()
                .map(productConverter::convertFirstToSecond)
                .collect(Collectors.toList());
    }

    //method to get products from repository
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(ProductNotFoundException::new);

        return productConverter.convertFirstToSecond(product);

        /*
        Can't one just do this instead?

        return productConverter.convertFirstToSecond(productRepository.findProductById(id)
                .orElseThrow(ProductNotFoundException::new));
        */
    }

    //method to delete products
    public void deleteProductById(Long id) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }

    //method to save products
    public void saveProduct(@Valid ProductDTO productDTO) {
        //converting ProductDTO object to Product
        Product product = productConverter.convertSecondToFirst(productDTO);

        //CHANGE PROD CONV SO U DONT HAVE TO DO THIS HERE
        //INJECT NEEDED REPOSITORY THERE, KEEP CODE CLEEN
        //------------------------------------------------------------------------------------------------------------!!
        //setting the seller by getting seller from sellerRepository
        //and afterward setting it into the seller field of the product
        //handling error in the process
        Seller seller = sellerRepository.findSellerById(productDTO.sellerId())
                .orElseThrow(SellerNotFoundException::new);
        product.setSeller(seller);

        //saving the product
        productRepository.save(product);
    }

    public boolean updateProductDetails(Long id, ProductDTO productDTO) {
        boolean productExisting = false;
        Optional<Product> productById = productRepository.findProductById(id);
        Product product = new Product();

        if (productById.isPresent()) {
            productExisting = true;
            product = productById.get();
        } else {
            product = new Product();
        }

        updateProduct(productDTO, product);
        productRepository.save(product);

        return productExisting;
    }

    private void updateProduct(ProductDTO productDTO, Product product) {
        if (productDTO.name() != null){
            product.setName(productDTO.name());
        }

        if (productDTO.manufacturer() != null){
            product.setManufacturer(productDTO.manufacturer());
        }

        if (productDTO.country() != null){
            product.setCountry(productDTO.country());
        }

        if (productDTO.price() != null){
            product.setPrice(productDTO.price());
        }

        if (productDTO.rating() != null){
            product.setRating(productDTO.rating());
        }

        if (productDTO.sellerId() != null){
            Seller seller = sellerRepository.findSellerById(productDTO.sellerId())
                    .orElseThrow(SellerNotFoundException::new);

            product.setSeller(seller);
        }
    }


}
