package com.tema.tema.converters.impl;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.models.dtos.ProductDTO;
import com.tema.tema.models.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements ObjectConverter<Product, ProductDTO> {


    //sellerId is not set here but in the method which uses
    //the converter in ProductService
    //this is done to keep the code as clean as possible
    //and to use as few instances of repositories as possible
    //same thing can be seen in OrderConverter
    @Override
    public Product convertSecondToFirst(ProductDTO dto) {
        Product p = new Product();

        p.setName(dto.name());
        p.setManufacturer(dto.manufacturer());
        p.setCountry(dto.country());
        p.setPrice(dto.price());
        p.setRating(dto.rating());

        return p;
    }

    @Override
    public ProductDTO convertFirstToSecond(Product p) {
        return ProductDTO.builder()
                .name(p.getName())
                .manufacturer(p.getManufacturer())
                .country(p.getCountry())
                .price(p.getPrice())
                .rating(p.getRating())
                .sellerId(p.getSeller().getId())
                .build();
    }
}
