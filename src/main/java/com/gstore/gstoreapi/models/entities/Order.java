package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.models.constants.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //order number, nothing fancy
    @NotNull
    @Column(name = "order number")
    private String orderNumber;

    //order status ()
    @NotNull
    @Column(name = "status")
    private OrderStatus status;

    //id of the buyer which placed the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;


    //list of products on particular order
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "orders_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;


        /*
        COME BACK TO THIS
        Instead of having a set for the products in an order i will instead use a map of some kind
        in order to also know how many of the same product the order contains, this will be mirrored
        in the orderDTO by two lists. they must be ordered in order to have correct knowledge regarding quantity.
        the index of the long type in the product id list will represent the product id
        and the same index in the other list will represent the quantity of the product
        referenced by the id on the index in the first list
         */

    //will add order placed date, and order completed date, ETA, etc.
}
