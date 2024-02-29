package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.models.dtos.SellerDTO;
import com.gstore.gstoreapi.models.entities.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerConverter implements ObjectConverter<Seller, SellerDTO> {

    @Override
    public Seller convertSecondToFirst(SellerDTO sellerDTO) {
        Seller seller = new Seller();

        seller.setName(sellerDTO.name());
        seller.setEmail(sellerDTO.email());
        seller.setCountry(sellerDTO.country());
        seller.setInternational(sellerDTO.international());
        seller.setStatus(sellerDTO.status());

        return seller;
    }

    @Override
    public SellerDTO convertFirstToSecond(Seller seller) {
        return SellerDTO.builder()
                .name(seller.getName())
                .email(seller.getEmail())
                .country(seller.getCountry())
                .international(seller.getInternational())
                .status(seller.getStatus())
                .build();
    }
}
