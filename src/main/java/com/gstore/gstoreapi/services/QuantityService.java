package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.converters.impl.QuantityConverter;
import com.gstore.gstoreapi.exceptions.ProductNotAvailableException;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.QuantityDTO;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.QuantityRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuantityService {

    private final QuantityRepository quantityRepository;
    private final QuantityConverter quantityConverter;


    public void saveQuantity(@Valid QuantityDTO quantityDTO, Order order) {
        Quantity quantity = quantityConverter.convertSecondToFirst(quantityDTO);

        if (quantity.getProduct() == null && quantityDTO.productId() != null) {
            throw new ProductNotFoundException(String.format("No product with id %d found!", quantityDTO.productId()));
        } else if (quantity.getProduct() == null) {
            throw new ValidationException("Product cannot be null!");
        } else if (Boolean.FALSE.equals(quantity.getProduct().getAvailable())) {
            throw new ProductNotAvailableException();
        }

        quantity.setOrder(order);
        quantityRepository.save(quantity);
    }

}
