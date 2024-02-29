package com.gstore.gstoreapi.repositories;

import com.gstore.gstoreapi.enums.ProductCategory;
import com.gstore.gstoreapi.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductById(Long id);

    @Query("select p from Product p where p.category = :category")
    List<Product> findAllByCategory(ProductCategory category);

}