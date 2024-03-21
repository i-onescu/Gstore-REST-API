package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.converters.impl.OrderConverter;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.exceptions.OrderNotFoundException;
import com.gstore.gstoreapi.enums.OrderStatus;
import com.gstore.gstoreapi.exceptions.OrderNotModifiableException;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.repositories.OrderRepository;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final QuantityService quantityService;
    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;


    //saves a new order based on a valid DTO
    //payload only contains quantities of products, the rest of the details are set in this method
    public void placeOrder(@Valid OrderDTO orderDTO) {
        //converts DTO into an instance of Order
        Order order = orderConverter.convertSecondToFirst(orderDTO);

        //sets placed datetime to this moment in time
        order.setPlacedDateTime(LocalDateTime.now());

        if (order.getBuyer() == null && orderDTO.buyerId() != null){
            throw new BuyerNotFoundException(orderDTO.buyerId());
        } else if (order.getBuyer() == null) {
            throw new ValidationException("Buyer cannot be null!");
        }

        //order number is generated
        order.setOrderNumber(generateOrderNum(order));

        //status is set to initial status of OrderStatus.PLACED
        order.setStatus(OrderStatus.PLACED);

        //price is calculated based on order quantities
        order.setPrice(order.getOrderQuantities().stream()
                .mapToDouble(quantity -> quantity.getQuantity() * quantity.getProduct().getPrice())
                .sum());

        //firstly, order is saved
        orderRepository.save(order);

        //logs new order id for testing purposes
        log.info("ID for newly created order is " + order.getId());

        //subsequently, details are saved with new persisted order inside them in order to persist relationship
        saveQuantitiesForOrder(order, orderDTO);
    }


    //saves the quantities after order has already been saved in order to persist relationship
    //between order and its quantities, relationship is mapped on quantity side (see Quantity class)
    private void saveQuantitiesForOrder(Order order, OrderDTO orderDTO) {
        if (orderDTO.orderQuantities() != null && !(orderDTO.orderQuantities().isEmpty())) {
            orderDTO.orderQuantities()
                    .forEach(quantityDto -> quantityService.saveQuantity(quantityDto, order));
        }
    }


    //returns list of all orders in database
    public List<OrderDTO> getAllOrders() {
        //list of all entities found in repository
        List<Order> orders = orderRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return orders.stream()
                .map(orderConverter::convertFirstToSecond)
                .toList();
    }


    //retrieves specific order based on an id
    public OrderDTO getOrderById(Long id) {
        //throws exception if an order with the provided id is not found
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(()-> new OrderNotFoundException(id));

        //returns converted DTO
        return orderConverter.convertFirstToSecond(order);
    }


    //updates order details if an order for id passed as first argument is found
    //and updates it with details from DTO passed as second argument
    public void updateOrderDetails(Long id, OrderDTO orderDTO) {
        //firstly instantiate an optional with the value retrieved
        //after searching database for the order with the desired id
        Optional<Order> orderOptional = orderRepository.findOrderById(id);

        //secondly, do a quick verification to see if an order with the provided id is found
        //or if the order is modifiable at all (must have status of IN_PROGRESS or PLACED),
        //if not, throws exception thus saving time
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException(id);
        } else if (isModifiable(orderOptional.get())) {
            throw new OrderNotModifiableException();
        }
        //retrieves the order to be updated and instantiates a new order with details from the provided DTO
        Order order = orderOptional.get();
        Order patchOrder = orderConverter.convertSecondToFirst(orderDTO);

        //updates order with new details and saves changes in persistence layer
        updateOrder(order, patchOrder);
        orderRepository.save(order);
    }


    //updates order details
    //only updates what is allowed, does not change buyer, date placed, id
    public void updateOrder(Order order, Order patchOrder) {

        //not null validation was put in place because null/missing fields are allowed in payload,
        //as the client must provide only the fields and values that they wish to update for the chosen order

        if (patchOrder.getPrice() != null) {
            order.setPrice(patchOrder.getPrice());
        }

        if (patchOrder.getStatus() != null) {
            order.setStatus(patchOrder.getStatus());
        }
    }

    //cancels a specific order based on an id
    //do not necessarily want to delete orders as the history of buyer might come in handy
    public boolean cancelOrderById(Long id) {
        boolean alreadyCancelled = true;
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        //verifies if order is already cancelled to avoid useless database queries
        if (isModifiable(order)) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            alreadyCancelled = false;
        }
        return alreadyCancelled;
    }


    //generates order number based on datetime of order being placed and buyer id
    private String generateOrderNum(Order order) {
        return order.getPlacedDateTime().toString().replace(":", "")
                + order.getBuyer().getId() * 7;
    }


    private boolean isModifiable(Order order) {
        return order.getStatus() == OrderStatus.PLACED || order.getStatus() == OrderStatus.IN_PROGRESS;
    }

}