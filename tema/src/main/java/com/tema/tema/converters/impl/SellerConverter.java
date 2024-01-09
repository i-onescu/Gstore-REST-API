package com.tema.tema.converters.impl;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.models.dtos.SellerDTO;
import com.tema.tema.models.entities.Seller;
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
