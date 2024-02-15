package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.models.constants.OrderStatus;
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

    //id of the buyer which placed the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    //quantities of products in order
    @OneToMany(mappedBy = "order")
    private Set<Quantity> orderQuantities;

    //date and time date was placed
    @NotNull
    @Column(name = "placed_date_time", columnDefinition = "DATE")
    private LocalDateTime placedDateTime;

    //and order completed date, ETA, etc.

}
