package it.polito.SE2.P12.SPG.utils;

public class API {
    public static final String HOME = "/api";
    public static final String ALL_PRODUCT = "/product/all";
    public static final String EXIST_CUSTOMER = "/customer/customerExists";
    public static final String EXIST_CUSTOMER_BY_EMAIL = "/customer/customerExistsByMail";
    public static final String CREATE_CUSTOMER = "/customer/addCustomer";
    public static final String PLACE_ORDER = "/customer/placeOrder";
    public static final String ADD_TO_BASKET = "/product/addToCart";
    public static final String GET_CART = "/customer/getCart";
    public static final String GET_WALLET = "/customer/getWallet";
    public static final String TOP_UP = "/customer/topUp";
    public static final String DELIVER_ORDER = "/customer/deliverOrder";
    public static final String DROP_ORDER = "/customer/dropOrder";
    public static final String GET_ORDERS = "/customer/getAllOrders";
    public static final String GET_ORDERS_BY_EMAIL = "/customer/getOrdersByEmail";
    public static final String RETRIEVE_ERROR = "/customer/retrieveError";
    public static final String EXPECTED_PRODUCTS = "/product/expectedProducts";
    public static final String REPORT_EXPECTED = "/farmer/reportExpected";
    public static final String TEST = "test";
    public static final String REFRESH_TOKEN = "/token/refresh";
    public static final String LOGOUT = "/logout";
}
