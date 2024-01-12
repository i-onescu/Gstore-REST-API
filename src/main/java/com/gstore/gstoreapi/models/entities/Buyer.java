package com.gstore.gstoreapi.models.entities;

import com.gstore.gstoreapi.models.entityParents.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
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

    //list of orders customer has made, a history
    @OneToMany(mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<Order> orders;

    //will add address, status of acc etc.

}
