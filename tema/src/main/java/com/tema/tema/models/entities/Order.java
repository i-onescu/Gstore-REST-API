package com.tema.tema.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    //order number, nothing fancy
    @NotNull
    @Column(name = "order number")
    private String orderNumber;

    //id of the buyer which placed the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    //list of products on particular order
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    //will probably add by enum class (PLACED, IN_PROGRESS/SHIPPING, COMPLETED)
    //will add order placed date, and order completed date, ETA, etc.
}
