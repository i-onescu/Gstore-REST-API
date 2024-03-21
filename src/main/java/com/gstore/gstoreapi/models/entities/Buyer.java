package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.enums.AccountStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "buyers")
public class Buyer {

    @Transient
    private final long secretKey = 6413806169116570852l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z]+")
    @Column(name = "name")
    private String name;

    //country in which buyer is located, serves as address for now
    @NotNull
    @Pattern(regexp = "[a-zA-Z]+")
    @Column(name = "country")
    private String country;

    //contact email and where buyer receives order info
    @NotNull
    @Pattern(regexp = ".+@.+\\..+")
    @Column(name = "email", unique = true)
    private String email;

    //age of buyer
    @NotNull
    @Range(min = 18, max = 120)
    @Column(name = "age")
    private Integer age;

    //status of buyer account
    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    //list of orders customer has made, a history
    @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER)
    private List<Order> orders;

    //cart contains items the buyer would like to purchase in an order
    //containing pseudo-quantities with a pattern of
    // item1Id:item1Quantity,item2Id:item2Quantity,item3Id:item3Quantity
    @Nullable
    @Column(name = "cart")
    private String cart;

    @NotNull
    @Column(name = "password")
    private String password;


    //CONSTRUCTORS ADDED FOR TESTING PURPOSES
    public Buyer() { }

    public Buyer(String name,
                 String country,
                 String email,
                 Integer age,
                 AccountStatus status,
                 String password) {
        this.name = name;
        this.country = country;
        this.email = email;
        this.age = age;
        this.status = status;
        this.password = password;
    }
}
