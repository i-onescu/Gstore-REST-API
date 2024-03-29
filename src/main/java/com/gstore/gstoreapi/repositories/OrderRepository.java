package com.gstore.gstoreapi.repositories;


import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findOrderById(Long id);

}