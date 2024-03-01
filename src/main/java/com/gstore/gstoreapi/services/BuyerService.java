package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.CartConverter;
import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.*;
import com.gstore.gstoreapi.enums.AccountStatus;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.dtos.LoginDTO;
import com.gstore.gstoreapi.models.dtos.OrderDTO;
import com.gstore.gstoreapi.models.dtos.QuantityDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.CustomSession;
import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import com.gstore.gstoreapi.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;
    private final ObjectConverter<Buyer, BuyerDTO> buyerConverter;
    private final ObjectConverter<Order, OrderDTO> orderConverter;
    private final CustomSessionService customSessionService;


    //saves a new buyer based on a provided DTO
    public BuyerDTO saveBuyer(@Valid BuyerDTO buyerDTO) {
        if (buyerRepository.existsByEmail(buyerDTO.email())) {
            throw new EmailTakenException();
        }

        //converts DTO to an instance of Buyer
        Buyer buyer = buyerConverter.convertSecondToFirst(buyerDTO);

        //status is set to AccountStatus.REGISTERED, activation to be determined
        buyer.setStatus(AccountStatus.REGISTERED);

        //new buyer is saved
        buyerRepository.save(buyer);

        return buyerConverter.convertFirstToSecond(buyer);
    }


    //returns list of all buyers in database
    public List<BuyerDTO> getAllBuyers() {
        //list of all entities found in repository
        List<Buyer> buyers = buyerRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return buyers.stream()
                .map(buyerConverter::convertFirstToSecond)
                .toList();
    }


    //retrieves a buyer from repository based on provided id
    public BuyerDTO getBuyerById(Long id) {
        //throws exception if a buyer with the provided id is not found
        Buyer buyer = buyerRepository.findBuyerById(id)
                .orElseThrow(BuyerNotFoundException::new);

        //returns a converted DTO
        return buyerConverter.convertFirstToSecond(buyer);
    }


    //returns list of all orders for a particular buyer stored in repository
    public List<OrderDTO> getBuyerOrders(Long sessionId) {
        CustomSession session = getSessionOrElseThrow(sessionId);
        Buyer buyer = session.getBuyer();

        return buyer.getOrders().stream()
                .map(orderConverter::convertFirstToSecond)
                .toList();
    }

    //method returning cart
    public List<QuantityDTO> goToCart(Long sessionId) {
        CustomSession session = getSessionOrElseThrow(sessionId);
        String cart = session.getBuyer().getCart();

        if (cart == null || cart.isEmpty()) {
            throw new EmptyCartException();
        }

        return CartConverter.convertCartStringToQuantityList(cart);
    }


    //method that adds products and their respective quantities to the cart
    public void addProductToCart(Long sessionId, QuantityDTO quantityDTO) {
        CustomSession session = getSessionOrElseThrow(sessionId);
        Product product = productRepository.findProductById(quantityDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException(String.format("No product with id %d found!",
                        quantityDTO.productId())));

        if (Boolean.FALSE.equals(product.getAvailable())) {
            throw new ProductNotAvailableException(String.format("Product with id %d currently unavailable for purchase!",
                    quantityDTO.productId()));
        }

        Buyer buyer = session.getBuyer();
        String cart = buyer.getCart();

        cart = updateCart(cart, quantityDTO);
        buyer.setCart(cart);

        buyerRepository.save(buyer);
    }

    //COME BACK AND FIX/CLEAN

    public void removeProductFromCart(Long sessionId, QuantityDTO quantityDTO) {
        CustomSession session = getSessionOrElseThrow(sessionId);

        Buyer buyer = session.getBuyer();
        String cart = buyer.getCart();

        //if cart is empty nothing can be removed, throw error
        if (cart == null || cart.isEmpty()) {
            throw new EmptyCartException();
        }

        List<QuantityDTO> quantities = CartConverter.convertCartStringToQuantityList(cart);

        //if cart only contains one item
        if (quantities.size() == 1 && quantityDTO.productId().equals(quantities.get(0).productId())) {
            //if client wants to reduce number of the same item in cart
            if (quantityDTO.quantity() < quantities.get(0).quantity()) {
                quantities.set(
                        0,
                        QuantityDTO.builder()
                                .productId(quantityDTO.productId())
                                .quantity(quantities.get(0).quantity() - quantityDTO.quantity())
                                .build());
            } else {
                //if client wants to empty cart
                quantities.clear();
            }
        } else {
            //if cart contains multiple items
            for (int i = 0; i < quantities.size(); i++) {
                //when the item that is wished to be removed is encountered
                if (quantityDTO.productId().equals(quantities.get(i).productId())) {
                    //if the number of items to be removed is greater opr equal to the quantity in cart
                    if (quantityDTO.quantity().equals(quantities.get(i).quantity()) || quantityDTO.quantity() > quantities.get(i).quantity()) {
                        //removes quantityDTO of specified item from array
                        quantities.remove(i);
                        break;
                    } else {
                        //modifies the number of single item to be less
                        quantities.set(
                                i,
                                QuantityDTO.builder()
                                        .productId(quantityDTO.productId())
                                        .quantity(quantities.get(i).quantity() - quantityDTO.quantity())
                                        .build());
                    }
                }
            }
        }




        //IMPORTANT DO NOT FORGET TO CHANGE THIS TO LIST OF QUANTITY DTO SO U DON'T HAVE TO DO THIS
        buyer.setCart(CartConverter.convertQuantityArrayToCartString(quantities));
        buyerRepository.save(buyer);

    }


    private String updateCart(String cart, QuantityDTO quantityDTO) {

        List<QuantityDTO> quantities = CartConverter.convertCartStringToQuantityList(cart);
        if (!quantities.isEmpty()) {
            quantities.set(0, quantityDTO);
            return CartConverter.convertQuantityArrayToCartString(quantities);
        }

        quantities.add(quantityDTO);
        return CartConverter.convertQuantityArrayToCartString(quantities);
    }


    //updates buyer from repository based on an id and DTO containing new details (both provided by client)
    public void updateBuyerDetails(Long sessionId, BuyerDTO buyerDTO) {
        Buyer buyer = getSessionOrElseThrow(sessionId).getBuyer();
        Buyer patchBuyer = buyerConverter.convertSecondToFirst(buyerDTO);

        //updates buyer with new details and saves changes in persistence layer
        updateBuyer(buyer, patchBuyer);
        buyerRepository.save(buyer);
    }


    //updates buyer details
    //only updates what is allowed, does not change id
    private void updateBuyer(Buyer buyer, Buyer patchBuyer) {

        //not null validation was put in place because null/missing fields are allowed in payload,
        //as the client must provide only the fields and values that they wish to update for the chosen buyer

        if (patchBuyer.getName() != null) {
            buyer.setName(patchBuyer.getName());
        }

        if (patchBuyer.getCountry() != null) {
            buyer.setCountry(patchBuyer.getCountry());
        }

        if (patchBuyer.getEmail() != null) {
            buyer.setEmail(patchBuyer.getEmail());
        }

        if (patchBuyer.getAge() != null) {
            buyer.setAge(patchBuyer.getAge());
        }

        if (patchBuyer.getStatus() != null) {
            buyer.setStatus(patchBuyer.getStatus());
        }

        if (patchBuyer.getPassword() != null) {
            buyer.setPassword(patchBuyer.getPassword());
        }
    }


    //deletes buyer from repository based on provided id
    public void deleteBuyerById(Long id) {
        //throws exception if a buyer with the provided id is not found
        Buyer buyer = buyerRepository.findBuyerById(id)
                .orElseThrow(BuyerNotFoundException::new);

        //deletes buyer
        buyerRepository.delete(buyer);
    }


    public Long login(LoginDTO loginDetails) {
        if (loginDetails.email().isBlank() || loginDetails.password().isBlank()) {
            throw new IncorrectLoginDetailsException("Fields cannot be empty!");
        }

        Optional<Buyer> buyerByLogin =
                buyerRepository.findBuyerByEmailAndPassword(loginDetails.email(), loginDetails.password());

        if (buyerByLogin.isPresent()) {
            Optional<CustomSession> sessionByBuyer = customSessionService.getCustomSessionByBuyer(buyerByLogin.get());

            if (sessionByBuyer.isPresent()) {
                throw new ExistingActiveSessionException(String.format("There is already an active session running!\n" +
                        "Existing session id is %d.", sessionByBuyer.get().getId()));
            } else {
                CustomSession cs = new CustomSession();
                cs.setBuyer(buyerByLogin.get());
                customSessionService.saveCustomSession(cs);
                return cs.getId();
            }
        } else throw new IncorrectLoginDetailsException("Username or password incorrect!");
    }


    public String logout(Long sessionId) {
        Optional<CustomSession> sessionById = customSessionService.getCustomSessionById(sessionId);
        if (sessionById.isEmpty()) {
            throw new InvalidSessionIdException(String.format("Session with id %d does not exist!", sessionId));
        } else {
            customSessionService.deleteCustomSession(sessionById.get());
            return "Successfully logged out!";
        }
    }


    private CustomSession getSessionOrElseThrow(Long sessionId) {
        if (sessionId == null) {
            throw new InvalidSessionIdException("Please provide a session id!");
        }

        Optional<CustomSession> sessionById = customSessionService.getCustomSessionById(sessionId);
        if (sessionById.isEmpty()) {
            throw new InvalidSessionIdException("Invalid session id!");
        }

        return sessionById.get();
    }


    public OrderDTO getCartCheckout(Long sessionId) {
        CustomSession session = getSessionOrElseThrow(sessionId);

        Set<QuantityDTO> quantitites = new HashSet<>(CartConverter.convertCartStringToQuantityList(session.getBuyer().getCart()));


        return OrderDTO.builder()
                .buyerId(session.getBuyer().getId())
                .orderQuantities(quantitites)
                .build();
    }



}

