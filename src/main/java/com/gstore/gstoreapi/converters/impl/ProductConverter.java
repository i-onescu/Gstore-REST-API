package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.models.entities.Seller;
import com.gstore.gstoreapi.repositories.SellerRepository;
import com.gstore.gstoreapi.services.SellerService;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements ObjectConverter<Product, ProductDTO> {

    SellerRepository sellerRepository;

    public ProductConverter(SellerRepository sellerService) {
        this.sellerRepository = sellerService;
    }

    //included validation just to do 2 in one
    @Override
    public Product convertSecondToFirst(ProductDTO dto) {
        Product p = new Product();

        if (dto.name() != null){
            p.setName(dto.name());
        }

        if (dto.manufacturer() != null){
            p.setManufacturer(dto.manufacturer());
        }

        if (dto.country() != null){
            p.setCountry(dto.country());
        }

        if (dto.price() != null){
            p.setPrice(dto.price());
        }

        if (dto.rating() != null){
            p.setRating(dto.rating());
        }

        //method throws SellerNotFoundException if seller id is not found
        if (dto.sellerId() != null){
            Seller seller = sellerRepository.findSellerById(dto.sellerId())
                    .orElseThrow(SellerNotFoundException::new);

            p.setSeller(seller);
        }

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
