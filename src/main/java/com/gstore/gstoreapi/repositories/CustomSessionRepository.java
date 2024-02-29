package com.gstore.gstoreapi.repositories;

import com.gstore.gstoreapi.models.entities.Buyer;
import com.gstore.gstoreapi.models.entities.CustomSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomSessionRepository extends JpaRepository<CustomSession, Long> {

    boolean existsByBuyer(Buyer buyer);

    Optional<CustomSession> findById(Long id);

    Optional<CustomSession> findByBuyer(Buyer buyer);

}
