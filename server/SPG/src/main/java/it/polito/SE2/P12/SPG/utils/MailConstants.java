package it.polito.SE2.P12.SPG.utils;

import it.polito.SE2.P12.SPG.entity.Customer;

public class MailConstants {
    private MailConstants() {
        throw new IllegalStateException("Mail constants utility class");
    }

    //top up reminder (body + subject)
    public static String EMAIL_MESSAGE_TOP_UP_REMINDER(String receiver, String sender) {
        return "Dear " + receiver.split("@")[0] + ",\nYour wallet amount is not sufficient to process the order(s), please don't forget to top-up your wallet otherwise your order will be cancelled." +
                "\n\nHave a nice day,\n" +
                sender + ", from SPG.";
    }

    public static String EMAIL_PICK_UP_WARNING(Customer customer) {
        return "Dear " + customer.getName() + ",\n" +
                "SPG wants to inform you that you have reached the " + customer.getMissedPickUpAmount() + "th missed pick up.\n" +
                "We are waiting fro you at our offices otherwise once you have reached the 5th you will be banned form SPG platform.\nBest regards and have a good day,\n\n\n***This is an automatically generated email, please do not respond to***"
                ;
    }

    public static final String EMAIL_SUBJECT_TOP_UP_REMINDER = "Cannot process order due to insufficient wallet amount";
    public static final String EMAIL_SUBJECT_WARNINGS_PICK_UP = "There pick up pending";
    public static final String NO_REPLY_EMAIL = "noReply@spg.com";

}
