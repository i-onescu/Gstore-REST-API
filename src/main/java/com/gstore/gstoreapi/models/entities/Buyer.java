package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.models.constants.AccountStatus;
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
    @Column(name = "email")
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
    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Order> orders;

    //will add address

}
