package com.tema.tema.services;

import com.tema.tema.converters.ObjectConverter;
import com.tema.tema.exceptions.SellerNotFoundException;
import com.tema.tema.repositories.SellerRepository;
import com.tema.tema.models.dtos.SellerDTO;
import com.tema.tema.models.entities.Seller;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
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

        sellerRepository.save(seller);
    }

    //updates seller details based on an id and DTO containing new details
    //returns true if the seller existed previously, if not returns false
    public boolean updateSellerDetails(Long id, SellerDTO sellerDTO) {
        boolean sellerExisting = false;
        Optional<Seller> sellerById= sellerRepository.findSellerById(id);
        Seller seller;

        if (sellerById.isPresent()) {
            sellerExisting = true;
            seller = sellerById.get();
        } else {
            seller = new Seller();
        }

        updateSeller(sellerDTO, seller);
        sellerRepository.save(seller);

        return sellerExisting;
    }

    //updates passed seller details based on DTO passed
    private void updateSeller(SellerDTO sellerDTO, Seller seller) {
        if (sellerDTO.name() != null) {
            seller.setName(sellerDTO.name());
        }

        if (sellerDTO.country() != null) {
            seller.setCountry(sellerDTO.country());
        }

        if (sellerDTO.email() != null) {
            seller.setEmail(sellerDTO.email());
        }

    }

}
