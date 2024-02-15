package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.QuantityDto;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.BuyerRepository;

import com.gstore.gstoreapi.repositories.QuantityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderConverter implements ObjectConverter<Order, OrderDTO> {

    private final BuyerRepository buyerRepository;
    private final QuantityRepository quantityRepository;
    private final ObjectConverter<Quantity, QuantityDto> quantityConverter;

    @Override
    public Order convertSecondToFirst(OrderDTO orderDTO) {
        Order order = new Order();

        order.setOrderNumber(orderDTO.orderNumber());
        order.setPrice(orderDTO.price());
        order.setStatus(orderDTO.status());

        order.setBuyer(buyerRepository.findBuyerById(orderDTO.buyerId())
                .orElseThrow(BuyerNotFoundException::new));

        order.setOrderQuantities(
                orderDTO.orderQuantities().stream()
                        .map(quantityConverter::convertSecondToFirst)
                        .collect(Collectors.toSet())
        );

        order.setPlacedDateTime(orderDTO.placedDateTime());

        return order;
    }

    @Override
    public OrderDTO convertFirstToSecond(Order order) {
        return OrderDTO.builder()
                .orderNumber(order.getOrderNumber())
                .price(order.getPrice())
                .buyerId(order.getBuyer().getId())
                .status(order.getStatus())
                .buyerId(order.getBuyer().getId())
                .orderQuantities(order.getOrderQuantities().stream()
                        .map(quantityConverter::convertFirstToSecond)
                        .collect(Collectors.toSet()))
                .placedDateTime(order.getPlacedDateTime())
                .build();
    }
}
