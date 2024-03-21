package com.gstore.gstoreapi.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 * Representing the sale of a specific product in a specific order
 * Contains the product sold, the order in which it was sold and the quantity
 */
@Getter
@Setter
@Entity
@Table(name = "order_quantities")
public class Quantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @NotNull
    @Range(min = 1, max = 100)
    @Column(name = "quantity")
    private Integer quantity;


    //CONSTRUCTORS ADDED FOR TESTING PURPOSES
    public Quantity() { }

    public Quantity(Integer quantity) {
        this.quantity = quantity;
    }
}
