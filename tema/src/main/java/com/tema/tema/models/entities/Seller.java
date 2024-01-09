package com.tema.tema.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "sellers")
public class  Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z]+")
    @Column(name = "name")
    private String name;

    //country where seller is based in
    @NotNull
    @Pattern(regexp = "[a-zA-Z]+")
    @Column(name = "country")
    private String country;

    //contact email
    @NotNull
    @Pattern(regexp = ".+@.+\\..+")
    @Column(name = "email")
    private String email;

    //regarding if seller ships internationally
    //may change to a list of countries seller ships to
    @NotNull
    @Column(name = "international")
    private boolean international;

    //will probably change with sales or ditch all together
    //because sales/orders will be listed in product entity
    //and i can get list of orders through a QUERY (i think)
//    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
//    private List<Order> orders;

}
