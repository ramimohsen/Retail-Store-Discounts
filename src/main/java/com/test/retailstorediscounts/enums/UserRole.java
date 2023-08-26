package com.test.retailstorediscounts.enums;


import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_EMPLOYEE,
    ROLE_AFFILIATE,
    ROLE_ADMIN,
    ROLE_CUSTOMER;

    public String cleanRolePrefix() {
        return this.toString().replaceFirst("^ROLE_", "");
    }
}
