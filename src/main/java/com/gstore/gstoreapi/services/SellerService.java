package com.gstore.gstoreapi.services;

import com.gstore.gstoreapi.converters.ObjectConverter;
import com.gstore.gstoreapi.exceptions.SellerNotFoundException;
import com.gstore.gstoreapi.models.constants.AccountStatus;
import com.gstore.gstoreapi.models.dtos.SellerDTO;
import com.gstore.gstoreapi.models.entities.Seller;
import com.gstore.gstoreapi.repositories.SellerRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    private final ObjectConverter<Seller, SellerDTO> sellerConverter;

    public SellerService (SellerRepository sellerRepository, ObjectConverter<Seller, SellerDTO> sellerConverter) {
        this.sellerRepository = sellerRepository;
        this.sellerConverter = sellerConverter;
    }

    //returns list of all sellers in repository
    public List<SellerDTO> getAllSellers() {
        //list of all entities found in repository
        List<Seller> sellers = sellerRepository.findAll();

        //stream converts entities into DTOs and maps into a list
        return sellers.stream()
                .map(sellerConverter::convertFirstToSecond)
                .toList();
    }

    //returns specific seller based on an id
    public SellerDTO getSellerById(Long id) {
        Seller seller = sellerRepository.findSellerById(id)
                .orElseThrow(SellerNotFoundException::new);

        return sellerConverter.convertFirstToSecond(seller);
    }

    //deletes specific seller based on an id
    public void deleteSellerById(Long id) {
        Seller seller = sellerRepository.findSellerById(id)
                .orElseThrow(SellerNotFoundException::new);

        sellerRepository.delete(seller);
    }

    //saves new seller passed through DTO
    public void saveSeller(@Valid SellerDTO sellerDTO) {
        Seller seller = sellerConverter.convertSecondToFirst(sellerDTO);
        seller.setStatus(AccountStatus.CREATED);

        sellerRepository.save(seller);
    }

    //updates seller details based on an id and DTO containing new details
    //returns true if the seller existed previously, if not returns false
    public void updateSellerDetails(Long id, SellerDTO sellerDTO) {
        if (sellerRepository.findSellerById(id).isEmpty()) {
            throw new SellerNotFoundException();
        }

        Seller seller = sellerRepository.findSellerById(id).get();
        Seller patchSeller = sellerConverter.convertSecondToFirst(sellerDTO);

        updateSeller(seller, patchSeller);
        sellerRepository.save(seller);
    }

    //updates passed seller details based on DTO passed
    private void updateSeller(Seller seller, Seller patchSeller) {
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

}
