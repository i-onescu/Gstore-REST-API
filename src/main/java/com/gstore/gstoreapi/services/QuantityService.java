package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.models.dtos.QuantityDto;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.QuantityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuantityService {

    private final QuantityRepository quantityRepository;
    private final ObjectConverter<Quantity, QuantityDto> quantityConverter;


    public void saveQuantity(@Valid QuantityDto quantityDto, Order order) {
        Quantity quantity = quantityConverter.convertSecondToFirst(quantityDto);
        quantity.setOrder(order);
        quantityRepository.save(quantity);
    }




}
