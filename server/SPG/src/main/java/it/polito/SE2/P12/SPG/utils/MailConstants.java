package it.polito.SE2.P12.SPG.utils;

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

    public static final String EMAIL_SUBJECT_TOP_UP_REMINDER = "Cannot process order due to insufficient wallet amount";
}
