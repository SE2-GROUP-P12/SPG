package it.polito.SE2.P12.SPG.utils;

public class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static long getCurrentTimestamp(){
        return (System.currentTimeMillis() / 1000L);
    }
}
