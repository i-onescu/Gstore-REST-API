package com.gstore.gstoreapi.models.entityParents;

import com.gstore.gstoreapi.models.constants.UserType;
import lombok.Data;

@Data
public class User {

    private UserType type;

    private String name;

    private String country;

    private String email;

    private String phoneNumber;

}
