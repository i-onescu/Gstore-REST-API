package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.QuantityDTO;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuantityConverter implements ObjectConverter<Quantity, QuantityDTO> {

    private final ProductRepository productRepository;


    @Override
    public Quantity convertSecondToFirst(QuantityDTO quantityDto) {
        Quantity quantity = new Quantity();

        quantity.setProduct(productRepository.findProductById(quantityDto.productId()).orElse(null));
        quantity.setQuantity(quantityDto.quantity());

        return quantity;
    }

    @Override
    public QuantityDTO convertFirstToSecond(Quantity quantity) {
        return QuantityDTO.builder()
                .productId(quantity.getProduct().getId())
                .quantity(quantity.getQuantity())
                .build();
    }
}
