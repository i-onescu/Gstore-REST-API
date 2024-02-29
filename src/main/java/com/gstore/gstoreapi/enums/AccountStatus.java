package com.gstore.gstoreapi.enums;

public enum AccountStatus {

    REGISTERED("REGISTERED"),
    ACTIVE("ACTIVE"),
    SUSPENDED("SUSPENDED"),
    DEACTIVATED("DEACTIVATED");

    final String name;

    AccountStatus(String name){
        this.name = name;
    }

}
