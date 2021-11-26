package it.polito.SE2.P12.SPG.utils;

public class API {
    public static final String HOME = "/api";    //Testing controller: OK
    public static final String ALL_PRODUCT = "/product/all";    //Testing controller: OK
    public static final String EXIST_CUSTOMER = "/customer/customerExists"; //Testing controller: OK
    public static final String CREATE_CUSTOMER = "/customer/addCustomer";   //Testing controller: OK
    public static final String EXIST_CUSTOMER_BY_EMAIL = "/customer/customerExistsByEmail"; //Testing controller: OK
    public static final String PLACE_ORDER = "/customer/placeOrder"; //Testing controller: OK
    public static final String ADD_TO_BASKET = "/product/addToCart";
    public static final String GET_CART = "/customer/getCart";
    public static final String GET_WALLET = "/customer/getWallet";
    public static final String TOP_UP = "/customer/topUp";
    public static final String DELIVER_ORDER = "/customer/deliverOrder";    //Testing controller: OK
    public static final String DROP_ORDER = "/customer/dropOrder";   //Testing controller: OK
    public static final String GET_ORDERS_BY_EMAIL = "/customer/getOrdersByEmail";  //Testing controller: OK
    public static final String GET_ORDERS = "/customer/getAllOrders";   //Testing controller: OK
    public static final String TEST = "test";
    public static final String REFRESH_TOKEN = "/token/refresh";
    public static final String LOGOUT = "/logout";
}
