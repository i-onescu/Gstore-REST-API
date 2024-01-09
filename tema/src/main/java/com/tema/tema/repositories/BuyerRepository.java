package com.tema.tema.repositories;

import com.tema.tema.models.entities.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findBuyerById(Long id);

}