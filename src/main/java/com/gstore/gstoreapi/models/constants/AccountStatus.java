package com.gstore.gstoreapi.models.constants;

public enum AccountStatus {

    CREATED("CREATED"),
    ACTIVE("ACTIVE"),
    SUSPENDED("SUSPENDED"),
    DEACTIVATED("DEACTIVATED");

    final String name;

    AccountStatus(String name){
        this.name = name;
    }

}
