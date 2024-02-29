package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
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

    //order total price
    @NotNull
    @Column(name = "total")
    private Double price;

    //order status
    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //date and time date was placed
    @NotNull
    @Column(name = "placed_date_time")
    private LocalDateTime placedDateTime;

    //id of the buyer which placed the order
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    //quantities of products in order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Quantity> orderQuantities;



    //and order completed date, ETA, etc.

}
