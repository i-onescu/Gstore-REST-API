package com.tema.tema.services;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.exceptions.BuyerNotFoundException;
import com.tema.tema.models.dtos.BuyerDTO;
import com.tema.tema.models.entities.Buyer;
import com.tema.tema.repositories.BuyerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BuyerService {

    private final BuyerRepository buyerRepository;

    private final ObjectConverter<Buyer, BuyerDTO> buyerConverter;

    public BuyerService(BuyerRepository buyerRepository,
                        ObjectConverter<Buyer, BuyerDTO> buyerConverter) {
        this.buyerRepository = buyerRepository;
        this.buyerConverter = buyerConverter;
    }

    //returns list of all buyers in repository
    public List<BuyerDTO> getAllBuyers() {
        //list of all entities found in repository
        List<Buyer> buyers = buyerRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return buyers.stream()
                .map(buyerConverter::convertFirstToSecond)
                .collect(Collectors.toList());
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

        buyerRepository.save(buyer);
    }

    //updates buyer details based on an id and DTO containing new details
    //returns true if the seller existed previously, if not returns false
    public boolean updateBuyerDetails(Long id, BuyerDTO buyerDTO) {
        boolean buyerExisting = false;
        Optional<Buyer> buyerById = buyerRepository.findBuyerById(id);
        Buyer buyer;

        if (buyerById.isPresent()) {
            buyerExisting = true;
            buyer = buyerById.get();
        } else {
            buyer = new Buyer();
        }

        updateBuyer(buyerDTO, buyer);
        buyerRepository.save(buyer);

        return buyerExisting;
    }

    //updates passed buyer details based on DTO passed
    private void updateBuyer(BuyerDTO buyerDTO, Buyer buyer) {
        if (buyerDTO.name() != null){
            buyer.setName(buyerDTO.name());
        }

        if (buyerDTO.country() != null){
            buyer.setCountry(buyerDTO.country());
        }

        if (buyerDTO.email() != null){
            buyer.setEmail(buyerDTO.email());
        }

        if (buyerDTO.age() != null){
            buyer.setAge(buyerDTO.age());
        }
    }


}
