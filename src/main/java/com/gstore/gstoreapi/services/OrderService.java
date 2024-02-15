package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.OrderNotFoundException;
import com.gstore.gstoreapi.models.constants.OrderStatus;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.QuantityDto;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Quantity;
import com.gstore.gstoreapi.repositories.OrderRepository;
import com.gstore.gstoreapi.repositories.QuantityRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final QuantityService quantityService;
    private final OrderRepository orderRepository;
    private final ObjectConverter<Order, OrderDTO> orderConverter;
    private static  String orderNum = "ABC123";

    //returns all orders
    public List<OrderDTO> getAllOrders() {
        //list of all entities found in repository
        List<Order> orders = orderRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return orders.stream()
                .map(orderConverter::convertFirstToSecond)
                .toList();
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
    public void placeOrder(@Valid OrderDTO orderDTO) {
        Order order = orderConverter.convertSecondToFirst(orderDTO);

        order.setPlacedDateTime(LocalDateTime.now());
        order.setOrderNumber(orderNum);
        order.setStatus(OrderStatus.PLACED);
        order.setPrice(order.getOrderQuantities().stream()
                .mapToDouble(quantity -> quantity.getQuantity() * quantity.getProduct().getPrice())
                .sum());

        orderRepository.save(order);
        order = orderRepository.findOrderByBuyerAndPlacedDateTime(order.getBuyer(), order.getPlacedDateTime())
                .orElseThrow(OrderNotFoundException::new);

        saveQuantityForOrder(order, orderDTO);
    }


    public void updateOrderDetails(Long id, OrderDTO orderDTO) {
        if (orderRepository.findOrderById(id).isEmpty()) {
            throw new OrderNotFoundException();
        }
        Order order = orderRepository.findOrderById(id).get();
        Order patchOrder = orderConverter.convertSecondToFirst(orderDTO);

        updateOrder(order, patchOrder);
        orderRepository.save(order);
    }


    public void updateOrder(Order order, Order patchOrder) {
        if (patchOrder.getPrice() != null) {
            order.setPrice(patchOrder.getPrice());
        }

        if (patchOrder.getStatus() != null) {
            order.setStatus(patchOrder.getStatus());
        }

        if (patchOrder.getOrderQuantities() != null) {
            order.setOrderQuantities(patchOrder.getOrderQuantities());
        }
    }

    private void saveQuantityForOrder(Order order, OrderDTO orderDTO) {
        orderDTO.orderQuantities()
                .forEach(quantityDto -> quantityService.saveQuantity(quantityDto, order));
    }


}