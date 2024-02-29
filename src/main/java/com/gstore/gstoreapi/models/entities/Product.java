package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;


@Getter
@Setter
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
    private Boolean available;

    //product rating by customers who purchased said product
    @Range(min = 1, max = 10)
    @Column(name = "rating")
    private Integer rating;

    //seller of the product
    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    //search category for product
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductCategory category;


}
