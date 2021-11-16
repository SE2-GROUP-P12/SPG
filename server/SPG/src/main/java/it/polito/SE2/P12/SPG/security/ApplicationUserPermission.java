package it.polito.SE2.P12.SPG.security;


import java.util.Set;

//BASED ON ENTITY (define all permission)
//TODO: to implement db loading

/**
 * PERMISSIONs<->METHOD MAPPING:
 * GET : READ
 * POST : WRITE
 * DELETE : WRITE
 * UPDATE : WRITE
 */
public enum ApplicationUserPermission {
    PRODUCT_READ("product:read"),
    PRODUCT_WRITE("product:write"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:read"),
    FARMER_READ("farmer:read"),
    FARMER_WRITE("farmer:read"),
    ORDER_READ("order:read"),
    ORDER_WRITE("order:write"),

    ;

    //etc...

    private final String permissions;

    ApplicationUserPermission(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions(){
        return this.permissions;
    }
}
