package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.converters.impl.SellerConverter;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.enums.AccountStatus;
import com.gstore.gstoreapi.models.dtos.SellerDTO;
import com.gstore.gstoreapi.models.entities.Seller;
import com.gstore.gstoreapi.repositories.SellerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final SellerConverter sellerConverter;


    //saves a new seller based on a provided DTO
    public void saveSeller(@Valid SellerDTO sellerDTO) {
        //converts DTO to an instance of Seller
        Seller seller = sellerConverter.convertSecondToFirst(sellerDTO);

        //status is set to AccountStatus.REGISTERED, activation to be determined
        seller.setStatus(AccountStatus.REGISTERED);

        //new seller is saved
        sellerRepository.save(seller);
    }


    //returns list of all sellers in database
    public List<SellerDTO> getAllSellers() {
        //list of all entities found in repository
        List<Seller> sellers = sellerRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return sellers.stream()
                .map(sellerConverter::convertFirstToSecond)
                .toList();
    }


    //retrieves a seller from repository based on provided id
    public SellerDTO getSellerById(Long id) {
        //throws exception if a seller with the provided id is not found
        Seller seller = sellerRepository.findSellerById(id)
                .orElseThrow(SellerNotFoundException::new);

        //returns a converted DTO
        return sellerConverter.convertFirstToSecond(seller);
    }


    //updates seller from repository based on an id and DTO containing new details (both provided by client)
    public void updateSellerDetails(Long id, SellerDTO sellerDTO) {
        //firstly, does a quick verification to see if a seller with the provided id is found
        //if not, throws exception thus saving time
        if (sellerRepository.findSellerById(id).isEmpty()) {
            throw new SellerNotFoundException();
        }

        //retrieves the seller to be updated and instantiates a new seller with details from the provided DTO
        Seller seller = sellerRepository.findSellerById(id).get();
        Seller patchSeller = sellerConverter.convertSecondToFirst(sellerDTO);

        //updates seller with new details and saves changes in persistence layer
        updateSeller(seller, patchSeller);
        sellerRepository.save(seller);
    }


    //updates seller details
    //only updates what is allowed, does not change id
    private void updateSeller(Seller seller, Seller patchSeller) {

        //not null validation was put in place because null/missing fields are allowed in payload,
        //as the client must provide only the fields and values that they wish to update for the chosen seller

        if (patchSeller.getName() != null) {
            seller.setName(patchSeller.getName());
        }

        if (patchSeller.getCountry() != null) {
            seller.setCountry(patchSeller.getCountry());
        }

        if (patchSeller.getEmail() != null) {
            seller.setEmail(patchSeller.getEmail());
        }

        if (patchSeller.getInternational() != null) {
            seller.setInternational(patchSeller.getInternational());
        }
    }


    //deletes specific seller based on an id
    public void deleteSellerById(Long id) {
        //throws exception if a seller with the provided id is not found
        Seller seller = sellerRepository.findSellerById(id)
                .orElseThrow(SellerNotFoundException::new);

        //deletes seller
        sellerRepository.delete(seller);
    }

}
