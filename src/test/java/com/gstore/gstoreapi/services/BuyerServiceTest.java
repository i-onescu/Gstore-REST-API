package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.impl.BuyerConverter;
import com.gstore.gstoreapi.converters.impl.OrderConverter;
import com.gstore.gstoreapi.enums.AccountStatus;
import com.gstore.gstoreapi.enums.OrderStatus;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.CustomSession;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import com.gstore.gstoreapi.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuyerServiceTest {

    @InjectMocks
    private BuyerService buyerService;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BuyerConverter buyerConverter;
    @Mock
    private OrderConverter orderConverter;
    @Mock
    private CustomSessionService customSessionService;


    @Test
    void testSaveBuyer() {

        Buyer buyer = buildBuyer();
        BuyerDTO buyerDTO = buildBuyerDTO();

        when(buyerRepository.save(any(Buyer.class))).thenReturn(buyer);
        when(buyerConverter.convertFirstToSecond(buyer)).thenReturn(buyerDTO);
        when(buyerConverter.convertSecondToFirst(buyerDTO)).thenReturn(buyer);


        BuyerDTO result = buyerService.saveBuyer(buyerDTO);
        assertEquals(buyerDTO, result);

        verify(buyerRepository).save(any());
    }

    @Test
    void getAllBuyers() {

        List<BuyerDTO> buyerDTOS = List.of(
                BuyerDTO.builder()
                        .name("John")
                        .country("UK")
                        .email("johndoe@test.com")
                        .age(40)
                        .status(AccountStatus.ACTIVE)
                        .password(null)
                        .build(),
                BuyerDTO.builder()
                        .name("Alice")
                        .country("UK")
                        .email("alicedoe@test.com")
                        .age(30)
                        .status(AccountStatus.ACTIVE)
                        .password(null)
                        .build(),
                BuyerDTO.builder()
                        .name("Larry")
                        .country("UK")
                        .email("larrydoe@test.com")
                        .age(25)
                        .status(AccountStatus.ACTIVE)
                        .password(null)
                        .build()
        );
        List<Buyer> buyers =
                List.of(
                        new Buyer("John",
                                "UK",
                                "johndoe@test.com",
                                40,
                                AccountStatus.ACTIVE,
                                "1234"),
                        new Buyer("Alice",
                                "UK",
                                "alicedoe@test.com",
                                30,
                                AccountStatus.ACTIVE,
                                "5678"),
                        new Buyer("Larry",
                                "UK",
                                "larrydoe@test.com",
                                25,
                                AccountStatus.ACTIVE,
                                "0987")
                );

        when(buyerRepository.findAll()).thenReturn(buyers);

        when(buyerConverter.convertFirstToSecond(any(Buyer.class))).thenCallRealMethod();
//        when(buyerService.getAllBuyers()).thenCallRealMethod();

        List<BuyerDTO> result = buyerService.getAllBuyers();

        assertEquals(buyerDTOS, result);

        verify(buyerRepository).findAll();
        verify(buyerConverter, times(3)).convertFirstToSecond(any());
    }

    @Test
    void getBuyerById() {
        //given
        long id = 1L;
        BuyerDTO buyerDTO = buildBuyerDTO();
        Buyer buyer = buildBuyer();

        //where
        when(buyerRepository.findBuyerById(1l)).thenReturn(Optional.of(buyer));

        when(buyerConverter.convertFirstToSecond(buyer)).thenReturn(buyerDTO);

        //then
        BuyerDTO result = buyerService.getBuyerById(id);

        assertEquals(buyerDTO, result);

        verify(buyerRepository).findBuyerById(anyLong());
        verify(buyerConverter).convertFirstToSecond(any());
    }

    @Test
    void getBuyerOrders() {
        LocalDateTime now = LocalDateTime.now();

        Buyer buyer = buildBuyer();
        buyer.setId(1L);

        CustomSession session = new CustomSession();
        session.setId(1L);
        session.setBuyer(buyer);


        Order order = new Order("12345", 222D, OrderStatus.PLACED, now, buyer);
        OrderDTO orderDTO = OrderDTO.builder()
                .buyerId(1L)
                .orderNumber("12345")
                .price(222D)
                .status(OrderStatus.PLACED)
                .placedDateTime(now)
                .buyerId(1L)
                .orderQuantities(null)
                .build();

        List<Order> orders = List.of(order, order);
        buyer.setOrders(orders);

        List<OrderDTO> orderDTOS = List.of(orderDTO, orderDTO);


        when(customSessionService.getCustomSessionById(1L)).thenReturn(Optional.of(session));
        when(orderConverter.convertFirstToSecond(any())).thenReturn(orderDTO);

        List<OrderDTO> result = buyerService.getBuyerOrders(1L);

        assertEquals(orderDTOS, result);


    }

    @Test
    void goToCart() {



    }

    @Test
    void addProductToCart() {
    }

    @Test
    void updateCart() {
    }

    @Test
    void updateBuyerDetails() {
    }

    @Test
    void deleteBuyerById() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }


    private static Buyer buildBuyer() {
        Buyer buyer = new Buyer();

        buyer.setName("name");
        buyer.setCountry("country");
        buyer.setEmail("testserv@gmail.com");
        buyer.setAge(30);
        buyer.setStatus(AccountStatus.REGISTERED);
        buyer.setPassword("1234");

        return buyer;
    }

    private static BuyerDTO buildBuyerDTO() {
        return BuyerDTO.builder()
                .name("name")
                .country("country")
                .email("testserv@gmail.com")
                .age(30)
                .status(AccountStatus.REGISTERED)
                .password("1234")
                .build();
    }
}