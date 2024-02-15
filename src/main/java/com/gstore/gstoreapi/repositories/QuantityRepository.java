package com.gstore.gstoreapi.repositories;

import com.gstore.gstoreapi.models.entities.Order;
import com.gstore.gstoreapi.models.entities.Product;
import com.gstore.gstoreapi.models.entities.OrderQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderQuantityRepository extends JpaRepository<OrderQuantity, Long> {

    Optional<OrderQuantity> findSaleById(Long id);

    @Query("select s from OrderQuantity s where s.order = :o")
    Set<OrderQuantity> findSalesByOrder(Order o);

    @Query("select s from OrderQuantity s where s.product= :p")
    Set<OrderQuantity> findSalesByProduct(Product p);

}
