package com.gstore.gstoreapi.repositories;

import com.gstore.gstoreapi.models.entities.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findBuyerById(Long id);

    @Query("select b from Buyer b where b.email = :email and b.password = :password")
    Optional<Buyer> findBuyerByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

}
