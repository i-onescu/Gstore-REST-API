package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.exceptions.OrderNotFoundException;
import com.gstore.gstoreapi.exceptions.ProductNotFoundException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.ProductDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import com.gstore.gstoreapi.repositories.OrderRepository;
import com.gstore.gstoreapi.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService{

    private final OrderRepository orderRepository;
    private final ObjectConverter<Order, OrderDTO> orderConverter;

    public OrderService(OrderRepository orderRepository,
                        BuyerRepository buyerRepository,
                        ProductRepository productRepository,
                        ObjectConverter<Order, OrderDTO> orderConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
    }


    //returns all orders
    public List<OrderDTO> getAllOrders() {
        //list of all entities found in repository
        List<Order> orders = orderRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return orders.stream()
                .map(orderConverter::convertFirstToSecond)
                .collect(Collectors.toList());
    }


    //gets specific order based on an id
    //repository searches for entity with specific id and converts into DTO
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(OrderNotFoundException::new);

        return orderConverter.convertFirstToSecond(order);
    }


    //deletes a specific order based on an id
    public void deleteOrderById(Long id) {
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(OrderNotFoundException::new);

        orderRepository.delete(order);
    }


    //saves a new order based on a DTO
    public void saveOrder(@Valid OrderDTO orderDTO) {
        Order order = orderConverter.convertSecondToFirst(orderDTO);

        orderRepository.save(order);
    }

//
//    //updates a specific orders details based on an id and DTO containing new details
//    public void updateOrderDetails(Long id, OrderDTO orderDTO) {
//        if (orderRepository.existsById(id)) {
//            Order order = orderConverter.convertSecondToFirst(orderDTO);
//            orderRepository.save(order);
//        } else throw (new OrderNotFoundException());
//    }


    public void updateOrderDetails(Long id, OrderDTO orderDTO) {
        if (orderRepository.findOrderById(id).isPresent()) {
            Order order = orderRepository.findOrderById(id).get();
            Order patchOrder = orderConverter.convertSecondToFirst(orderDTO);

            updateOrder(order, patchOrder);
            orderRepository.save(order);
        } else throw (new OrderNotFoundException());
    }


    public void updateOrder(Order order,  Order patchOrder) {

        if (patchOrder.getOrderNumber() != null) {
            order.setOrderNumber(patchOrder.getOrderNumber());
        }

        if (patchOrder.getBuyer() != null) {
            order.setBuyer(patchOrder.getBuyer());
        }

        if (patchOrder.getProducts() != null) {
            order.setProducts(patchOrder.getProducts());
        }
    }

}