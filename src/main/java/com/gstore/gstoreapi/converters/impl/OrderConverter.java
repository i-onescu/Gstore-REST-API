package com.gstore.gstoreapi.converters.impl;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import com.gstore.gstoreapi.repositories.ProductRepository;
import com.gstore.gstoreapi.repositories.SellerRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class OrderConverter implements ObjectConverter<Order, OrderDTO> {
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;

    public OrderConverter(SellerRepository sellerRepository,
                          BuyerRepository buyerRepository, ProductRepository productRepository) {
        this.buyerRepository = buyerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order convertSecondToFirst(OrderDTO orderDTO) {
        Order o = new Order();

        if (orderDTO.orderNumber() != null) {
            o.setOrderNumber(orderDTO.orderNumber());
        }

        //throws BuyerNotFoundException if buyer id in DTO does not exist
        if (orderDTO.buyerId() != null) {
            Buyer b = buyerRepository.findBuyerById(orderDTO.buyerId()).
                    orElseThrow(BuyerNotFoundException::new);
            o.setBuyer(b);
        }

        if (orderDTO.status() != null) {
            o.setStatus(orderDTO.status());
        }

        if (orderDTO.productIds() != null) {
            long count = orderDTO.productIds().stream()
                    .filter(productRepository::existsById)
                    .count();

            if (count == orderDTO.productIds().size()) {
                List<Product> p = new ArrayList<>(productRepository.findAllById(orderDTO.productIds()));
                o.setProducts(p);
            } else throw (new ProductNotFoundException());
        }

        return o;
    }

    @Override
    public OrderDTO convertFirstToSecond(Order order) {
        return OrderDTO.builder()
                .orderNumber(order.getOrderNumber())
                .buyerId(order.getBuyer().getId())
                .status(order.getStatus())
                .productIds(order.getProducts().stream()
                        .map(Product::getId)
                        .toList())
                .build();
    }
}
