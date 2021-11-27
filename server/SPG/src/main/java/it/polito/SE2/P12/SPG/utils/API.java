package it.polito.SE2.P12.SPG.utils;

public class API {
    public static final String HOME = "/api"; //ok
    public static final String ALL_PRODUCT = "/product/all"; //ok
    public static final String EXIST_CUSTOMER = "/customer/customerExists"; //ok
    public static final String EXIST_CUSTOMER_BY_EMAIL = "/customer/customerExistsByEmail";//ok
    public static final String CREATE_CUSTOMER = "/customer/addCustomer";//ok/
    public static final String PLACE_ORDER = "/customer/placeOrder";//ok
    public static final String ADD_TO_BASKET = "/product/addToCart"; //ok
    public static final String GET_CART = "/customer/getCart"; //ok
    public static final String GET_WALLET = "/customer/getWallet"; //ok
    public static final String TOP_UP = "/customer/topUp"; //ok
    public static final String DELIVER_ORDER = "/customer/deliverOrder"; //ok
    public static final String DROP_ORDER = "/customer/dropOrder"; //ok
    public static final String GET_ORDERS = "/customer/getAllOrders"; //ok
    public static final String GET_ORDERS_BY_EMAIL = "/customer/getOrdersByEmail"; //ok
    public static final String RETRIEVE_ERROR = "/customer/retrieveError";
    public static final String EXPECTED_PRODUCTS = "/product/expectedProducts";
    public static final String REPORT_EXPECTED = "/farmer/reportExpected";
    public static final String TEST = "test";
    public static final String REFRESH_TOKEN = "/token/refresh"; //ok
    public static final String LOGOUT = "/logout"; //ok
}
