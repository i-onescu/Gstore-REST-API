package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.BuyerNotFoundException;
import com.gstore.gstoreapi.models.constants.AccountStatus;
import com.gstore.gstoreapi.models.dtos.BuyerDTO;
import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.repositories.BuyerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerService {

    private final BuyerRepository buyerRepository;
    private final ObjectConverter<Buyer, BuyerDTO> buyerConverter;


    //returns list of all buyers in repository
    public List<BuyerDTO> getAllBuyers() {
        //list of all entities found in repository
        List<Buyer> buyers = buyerRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return buyers.stream()
                .map(buyerConverter::convertFirstToSecond)
                .toList();
    }

    //returns specific buyer based on an id
    public BuyerDTO getBuyerById(Long id) {
        Buyer buyer = buyerRepository.findBuyerById(id)
                .orElseThrow(BuyerNotFoundException::new);

        return buyerConverter.convertFirstToSecond(buyer);
    }

    //deletes specific buyer based on an id
    public void deleteBuyerById(Long id) {
        Buyer buyer = buyerRepository.findBuyerById(id)
                .orElseThrow(BuyerNotFoundException::new);

        buyerRepository.delete(buyer);
    }

    //saves new buyer through passed DTO
    public void saveBuyer(@Valid BuyerDTO buyerDTO) {
        Buyer buyer = buyerConverter.convertSecondToFirst(buyerDTO);
        buyer.setStatus(AccountStatus.ACTIVE);
        buyerRepository.save(buyer);
    }

    //updates buyer details based on an id and DTO containing new details
    //returns true if the seller existed previously, if not returns false
    public void updateBuyerDetails(Long id, BuyerDTO buyerDTO) {
        if (buyerRepository.findBuyerById(id).isEmpty()) {
            throw new BuyerNotFoundException();
        }

        Buyer buyer = buyerRepository.findBuyerById(id).get();
        Buyer patchBuyer = buyerConverter.convertSecondToFirst(buyerDTO);

        updateBuyer(buyer, patchBuyer);
        buyerRepository.save(buyer);
    }

    //updates passed buyer details based on DTO passed
    private void updateBuyer(Buyer buyer, Buyer patchBuyer) {
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
    }
}

