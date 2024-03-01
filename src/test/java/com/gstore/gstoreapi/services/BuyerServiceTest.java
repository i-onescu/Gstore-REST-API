package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.enums.AccountStatus;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import com.gstore.gstoreapi.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ExtendWith(MockitoExtension.class)
class BuyerServiceTest {

    @InjectMocks private BuyerService buyerService;
    @Mock private BuyerRepository buyerRepository;
    @Mock private ProductRepository productRepository;
    @Mock private ObjectConverter<Buyer, BuyerDTO> buyerConverter;
    @Mock private ObjectConverter<Order, OrderDTO> orderConverter;
    @Mock private CustomSessionService customSessionService;



    @Test
    void testSaveBuyer() {

        Buyer buyer = buildBuyer();
        BuyerDTO buyerDTO = buildBuyerDTO();

        when(buyerRepository.save(any(Buyer.class))).thenReturn(buyer);
        when(buyerConverter.convertSecondToFirst(any(BuyerDTO.class))).thenReturn(buyer);

        BuyerDTO savedBuyer = buyerService.saveBuyer(buyerDTO);

        Assertions.assertEquals(savedBuyer, buyerDTO);

        verify(buyerRepository).save(any());
    }

    @Test
    void getAllBuyers() {

        BuyerDTO buyerDTO = buildBuyerDTO();
        List<Buyer> buyers = Arrays.asList(buildBuyer(), buildBuyer());
        List<BuyerDTO> buyerDTOS = Arrays.asList(buildBuyerDTO(), buildBuyerDTO());

        when(buyerRepository.findAll()).thenReturn(buyers);
        when(buyerConverter.convertFirstToSecond(any(Buyer.class))).thenReturn(buyerDTO);

        List<BuyerDTO> result = buyerService.getAllBuyers();

        Assertions.assertEquals(buyerDTOS, result);

        verify(buyerRepository).findAll();
        verify(buyerConverter).convertFirstToSecond(any());
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

        Assertions.assertEquals(buyerDTO, result);

        verify(buyerRepository).findBuyerById(anyLong());
        verify(buyerConverter).convertFirstToSecond(any());
    }

    @Test
    void getBuyerOrders() {



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
        Buyer buyer =new Buyer();

        buyer.setName("name");
        buyer.setCountry("country");
        buyer.setEmail("testserv@gmail.com");
        buyer.setAge(30);
        buyer.setStatus(AccountStatus.REGISTERED);
        buyer.setPassword("1234");

        return buyer;
    }

    private static BuyerDTO buildBuyerDTO(){
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