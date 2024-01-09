package com.tema.tema.converters.impl;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.models.dtos.BuyerDTO;
import com.tema.tema.models.entities.Buyer;
import org.springframework.stereotype.Component;

@Component
public class BuyerConverter implements ObjectConverter<Buyer, BuyerDTO> {


    @Override
    public Buyer convertSecondToFirst(BuyerDTO buyerDTO) {
        Buyer buyer = new Buyer();

        buyer.setName(buyerDTO.name());
        buyer.setCountry(buyerDTO.country());
        buyer.setEmail(buyerDTO.email());
        buyer.setAge(buyerDTO.age());

        return buyer;
    }

    @Override
    public BuyerDTO convertFirstToSecond(Buyer buyer) {
        return BuyerDTO.builder()
                .name(buyer.getName())
                .country(buyer.getCountry())
                .email(buyer.getEmail())
                .age(buyer.getAge())
                .build();
    }
}
