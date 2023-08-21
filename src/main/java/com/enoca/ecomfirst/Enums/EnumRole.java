package com.enoca.ecomfirst.Enums;

public enum EnumRole {
    USER,
    CUSTOMER,
    ADMIN;

    public String getRole() {
        return this.name();
    }
}
