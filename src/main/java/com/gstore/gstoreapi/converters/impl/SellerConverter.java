package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.models.dtos.SellerDTO;
import com.gstore.gstoreapi.models.entities.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerConverter implements ObjectConverter<Seller, SellerDTO> {

    @Override
    public Seller convertSecondToFirst(SellerDTO sellerDTO) {
        Seller s = new Seller();

        s.setName(sellerDTO.name());
        s.setEmail(sellerDTO.email());
        s.setCountry(sellerDTO.country());
        s.setInternational(sellerDTO.international());

        return s;
    }

    @Override
    public SellerDTO convertFirstToSecond(Seller seller) {
        return SellerDTO.builder()
                .name(seller.getName())
                .email(seller.getEmail())
                .country(seller.getCountry())
                .international(seller.isInternational())
                .build();
    }
}
