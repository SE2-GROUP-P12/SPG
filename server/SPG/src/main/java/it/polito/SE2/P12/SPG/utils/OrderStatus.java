package it.polito.SE2.P12.SPG.utils;

public class OrderStatus {
    private OrderStatus() {
        throw new IllegalStateException("Order status utility class");
    }

    //Open: this should trigger the employee pending cancellation, the order can't be paid
    public static final String ORDER_STATUS_OPEN = "OPEN";
    //Confirmed: the order can be paid
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    //Cancelled: due to an unsuccessful payment: customer has a mail reminder btw he can restore his order to place it again
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    //Payed: order is pay when customer successfully attempts the payment. Then pack rests in the home store until customer retrieves it or until shipping phase
    public static final String ORDER_STATUS_PAID = "PAID";
    //Not Retrieved: order is paid but in from tuesday to friday a pickup is missed
    public static final String ORDER_STATUS_NOT_RETRIEVED = "NOT_RETRIEVED";
    //On delivery: it is on delivery when parcel leaves the local store for shipping
    public static final String ORDER_STATUS_ON_DELIVERY = "ON_DELIVERY";
    //Closed: the parcel is correctly between the customer hands
    public static final String ORDER_STATUS_CLOSED = "CLOSED";
}
