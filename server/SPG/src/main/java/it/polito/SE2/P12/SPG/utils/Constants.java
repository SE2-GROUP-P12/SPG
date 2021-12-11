package it.polito.SE2.P12.SPG.utils;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Constants utility class");
    }

    /**
     * AUTHENTICATION AND AUTHORIZATION CONSTANTS
     */
    public static final Integer EXPIRED_INTERVAL_TIME_ACCESS_TOKEN = (60 * 10 * 1000); //is in milliseconds
    public static final Integer EXPIRED_INTERVAL_TIME_REFRESH_TOKEN = (60 * 10 * 3 * 24 * 1000); //is in milliseconds
    public static final String SERVER_SECRET = "SERVER_S_SECRET_FOR_JWT";
    public static final String HEADER_BEARER = "Bearer ";
    public static final String HEADER_ERROR = "error";


    /**
     * Json body constants
     */
    public static final String JSON_EXIST = "exist";
    public static final String JSON_ERROR_MESSAGE = "errorMessage";
    public static final String JSON_EMAIL = "email";
    public static final String JSON_PRODUCT_ID = "productId";
    public static final String JSON_ORDER_ID = "orderId";
    public static final String JSON_QUANTITY = "quantity";
    public static final String JSON_ROLES = "roles";
    public static final String JSON_DATE = "date";
    public static final String JSON_TIME = "time";
    public static final String JSON_DELIVERY_DATE = "deliveryDate";
    public static final String JSON_DELIVERY_ADDRESS = "deliveryAddress";

    /**
     * User emails in database
     */
    public static final String EMAIL_ADMIN = "admin@foomail.com";
    public static final String EMAIL_MARIO_ROSSI = "mario.rossi@gmail.com";
    public static final String EMAIL_PAOLO_BIANCHI = "paolo.bianchi@gmail.com";
    public static final String EMAIL_FRANCESCO_CONTE = "francesco.conte@gmail.com";
    public static final String EMAIL_THOMAS_JEFFERSON = "thomas.jefferson@gmail.com";
    public static final String EMAIL_ALEXANDER_HAMILTON = "alexander.hamilton@yahoo.com";
}
