package com.tema.tema.services;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.exceptions.BuyerNotFoundException;
import com.tema.tema.exceptions.OrderNotFoundException;
import com.tema.tema.exceptions.SellerNotFoundException;
import com.tema.tema.models.dtos.OrderDTO;
import com.tema.tema.models.entities.Buyer;
import com.tema.tema.models.entities.Order;
import com.tema.tema.models.entities.Product;
import com.tema.tema.models.entities.Seller;
import com.tema.tema.repositories.BuyerRepository;
import com.tema.tema.repositories.OrderRepository;
import com.tema.tema.repositories.ProductRepository;
import com.tema.tema.repositories.SellerRepository;
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
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;
    private final ObjectConverter<Order, OrderDTO> orderConverter;


    //injected all required repositories
    public OrderService(OrderRepository orderRepository,
                        BuyerRepository buyerRepository,
                        ProductRepository productRepository,
                        ObjectConverter<Order, OrderDTO> orderConverter) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.productRepository = productRepository;
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

    //updates a specific orders details based on an id and DTO containing new details
    public boolean updateOrderDetails(Long id, OrderDTO orderDTO) {
        boolean orderExisting = false;
        Optional<Order> orderById= orderRepository.findOrderById(id);
        Order order;

        if (orderById.isPresent()) {
            orderExisting = true;
            order = orderById.get();
        } else {
            order = new Order();
        }

        updateOrder(orderDTO, order);
        orderRepository.save(order);

        return orderExisting;
    }

    //updates passed order details based on DTO passed
    //made use of other entity repositories in order to save
    private void updateOrder(OrderDTO orderDTO, Order order) {
        if (orderDTO.orderNumber() != null) {
            order.setOrderNumber(orderDTO.orderNumber());
        }

        //throws BuyerNotFoundException if buyer id in DTO does not exist
        if (orderDTO.buyerId() != null) {
            Buyer b = buyerRepository.findBuyerById(orderDTO.buyerId()).
                    orElseThrow(BuyerNotFoundException::new);
            order.setBuyer(b);
        }

        if (orderDTO.productIds() != null) {
            Set<Product> p = new HashSet<>(productRepository.findAllById(orderDTO.productIds()));

            order.setProducts(p);
        }
    }




}