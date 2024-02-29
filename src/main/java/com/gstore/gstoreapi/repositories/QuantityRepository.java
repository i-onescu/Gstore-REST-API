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

    Optional<Quantity> findQuantityById(Long id);

    //finds all quantities of a certain product only if the order containing said quantity is not cancelled
    @Query("select q from Quantity q where q.product= :p and not q.order.status = 'CANCELED'")
    Set<Quantity> findQuantitiesByProduct(Product p);

}
