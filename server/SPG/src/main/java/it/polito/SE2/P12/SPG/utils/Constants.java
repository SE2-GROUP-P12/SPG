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

}
