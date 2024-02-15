package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.models.constants.AccountStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sellers")
public class  Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z ]+")
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
    @Column(name = "international")
    private Boolean international;

    //status of seller account
    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;



}
