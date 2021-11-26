package it.polito.SE2.P12.SPG.utils;

import java.util.List;

public class UserRole {

    public static final String ROLE_SHOP_EMPLOYEE = "EMPLOYEE";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_FARMER = "FARMER";

    public static final List<String> ROLE_BASKETUSER = List.of(ROLE_CUSTOMER, ROLE_SHOP_EMPLOYEE);
    public static final List<String> ROLE_ORDERUSER = List.of(ROLE_CUSTOMER);
}
