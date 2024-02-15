package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.models.constants.AccountStatus;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
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
        buyer.setStatus(buyerDTO.status());

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

