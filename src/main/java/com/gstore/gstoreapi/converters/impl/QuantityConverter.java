package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.QuantityDto;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.ProductRepository;
import com.gstore.gstoreapi.repositories.QuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuantityConverter implements ObjectConverter<Quantity, QuantityDto> {

    private final ProductRepository productRepository;
    private final QuantityRepository quantityRepository;



    //find a way to save the order id in the quantity
    @Override
    public Quantity convertSecondToFirst(QuantityDto quantityDto) {
        Quantity quantity = new Quantity();

        quantity.setProduct(productRepository.findProductById(quantityDto.productId())
                .orElseThrow(ProductNotFoundException::new));
        quantity.setQuantity(quantityDto.quantity());


        return quantity;
    }

    @Override
    public QuantityDto convertFirstToSecond(Quantity quantity) {
        return QuantityDto.builder()
                .productId(quantity.getProduct().getId())
                .quantity(quantity.getQuantity())
                .build();
    }
}
