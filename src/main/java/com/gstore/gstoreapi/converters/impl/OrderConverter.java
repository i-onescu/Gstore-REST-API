package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.QuantityDTO;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.BuyerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderConverter implements ObjectConverter<Order, OrderDTO> {

    private final BuyerRepository buyerRepository;
    private final QuantityConverter quantityConverter;

    @Override
    public Order convertSecondToFirst(OrderDTO orderDTO) {
        Order order = new Order();

        order.setOrderNumber(orderDTO.orderNumber());
        order.setPrice(orderDTO.price());
        order.setStatus(orderDTO.status());
        order.setPlacedDateTime(orderDTO.placedDateTime());
        order.setBuyer(buyerRepository.findBuyerById(orderDTO.buyerId()).orElse(null));


        if (orderDTO.orderQuantities() != null && !orderDTO.orderQuantities().isEmpty()) {
            order.setOrderQuantities(
                    orderDTO.orderQuantities().stream()
                            .map(quantityConverter::convertSecondToFirst)
                            .collect(Collectors.toSet())
            );
        }

        return order;
    }

    @Override
    public OrderDTO convertFirstToSecond(Order order) {
        return OrderDTO.builder()
                .orderNumber(order.getOrderNumber())
                .price(order.getPrice())
                .buyerId(order.getBuyer().getId())
                .status(order.getStatus())
                .orderQuantities(order.getOrderQuantities().stream()
                        .map(quantityConverter::convertFirstToSecond)
                        .collect(Collectors.toSet()))
                .placedDateTime(order.getPlacedDateTime())
                .build();
    }
}
