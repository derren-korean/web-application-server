package util;

public class ResponseHeaderSession {
    private static String contentType;

    private ResponseHeaderSession() {
    }

    public static void init() {
        setContentType("text/html");
    }

    public static String getContentType() {
        return contentType;
    }

    public static void setContentType(String type) {
        contentType = type;
    }
}
