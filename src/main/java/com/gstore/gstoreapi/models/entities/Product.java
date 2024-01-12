package com.gstore.gstoreapi.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]+", message = "Only letters allowed")
    @Column(name = "name")
    private String name;

    //name of manufacturer
    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z]+")
    @Column(name = "manufacturer")
    private String manufacturer;

    //country from which product is sold
    @NotNull
    @Pattern(regexp = "[a-zA-Z]+")
    @Column(name = "country")
    private String country;

    //product price
    @NotNull
    @Range(min = 1, max = 50000)
    @Column(name = "price")
    private Double price;

    //availability (true = in stock , false = not in stock )
    @NotNull
    @Column(name = "availability")
    private boolean available;

    //product rating by customers who purchased said product
    @Range(min = 1, max = 10)
    @Column(name = "rating")
    private Integer rating;

    //seller of the product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    //list of orders associated (!will probably change to sales!) ¯\_(ツ)_/¯
    @ManyToMany(mappedBy = "products")
    private Set<Order> orders;
}
