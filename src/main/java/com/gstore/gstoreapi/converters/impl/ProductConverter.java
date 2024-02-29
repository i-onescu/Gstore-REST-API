package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.enums.ProductCategory;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.repositories.SellerRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements ObjectConverter<Product, ProductDTO> {

    SellerRepository sellerRepository;

    public ProductConverter(SellerRepository sellerService) {
        this.sellerRepository = sellerService;
    }


    @Override
    public Product convertSecondToFirst(ProductDTO dto) {
        Product p = new Product();

        p.setName(dto.name());
        p.setManufacturer(dto.manufacturer());
        p.setCountry(dto.country());
        p.setPrice(dto.price());
        p.setRating(dto.rating());
        p.setAvailable(dto.available());

        if (dto.category() != null) {
            p.setCategory(ProductCategory.valueOf(dto.category().toUpperCase()));
        }

        //returning null because in case the client sends payload with a null seller
        //say in case of an update request, we can handle this case
        //and throw an exception only when we need a valid seller (ex: when saving a new product)
        p.setSeller(sellerRepository.findSellerById(dto.sellerId()).orElse(null));

        return p;
    }


    @Override
    public ProductDTO convertFirstToSecond(Product p) {
        return ProductDTO.builder()
                .Id(p.getId())
                .name(p.getName())
                .manufacturer(p.getManufacturer())
                .country(p.getCountry())
                .price(p.getPrice())
                .rating(p.getRating())
                .sellerId(p.getSeller().getId())
                .category(p.getCategory().name())
                .available(p.getAvailable())
                .build();
    }

}
