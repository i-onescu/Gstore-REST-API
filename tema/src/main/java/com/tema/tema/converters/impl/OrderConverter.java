package com.tema.tema.converters.impl;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.exceptions.BuyerNotFoundException;
import com.tema.tema.exceptions.SellerNotFoundException;
import com.tema.tema.models.dtos.OrderDTO;
import com.tema.tema.models.entities.Buyer;
import com.tema.tema.models.entities.Order;
import com.tema.tema.models.entities.Seller;
import com.tema.tema.repositories.BuyerRepository;
import com.tema.tema.repositories.SellerRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter implements ObjectConverter<Order, OrderDTO> {
    private final BuyerRepository buyerRepository;

    public OrderConverter(SellerRepository sellerRepository,
                          BuyerRepository buyerRepository) {
        this.buyerRepository = buyerRepository;
    }

    @Override
    public Order convertSecondToFirst(OrderDTO orderDTO) {
        Order o = new Order();

        o.setOrderNumber(orderDTO.orderNumber());

        Buyer b = buyerRepository.findBuyerById(orderDTO.buyerId())
                .orElseThrow(BuyerNotFoundException::new);
        o.setBuyer(b);

        //SAVE SET OF PRODUCTS!!!!
        //INJECT REPOSITORY
        //------------------------------------------------------------------------------------------------------------!!

        return o;
    }

    @Override
    public OrderDTO convertFirstToSecond(Order order) {
        return OrderDTO.builder()
                .orderNumber(order.getOrderNumber())
                .buyerId(order.getBuyer().getId())
                .build();
    }
}
