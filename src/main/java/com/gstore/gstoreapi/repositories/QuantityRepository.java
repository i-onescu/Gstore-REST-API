package com.gstore.gstoreapi.repositories;

import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.models.entities.Quantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface QuantityRepository extends JpaRepository<Quantity, Long> {

    Optional<Quantity> findSaleById(Long id);

    @Query("select s from Quantity s where s.product= :p")
    Set<Quantity> findSalesByProduct(Product p);

}
