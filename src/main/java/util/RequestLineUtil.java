package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestLineUtil {
    
    private static String BASE_FOLDER_NAME = "webapp";
    private static String DEFAULT_HTML_LOCATOR = "/index.html";
    public static String DEFAULT_URL = BASE_FOLDER_NAME + DEFAULT_HTML_LOCATOR;
    
    public static String getLine(InputStream in) {
        return requestLine(new BufferedReader(new InputStreamReader(in)));
    }

    private static String requestLine(BufferedReader requestBuffer) {
        return requestBuffer.lines().filter(line -> line.contains("HTTP")).findFirst().orElse(null);
    }

    public static boolean hasQuery(String requestLine) {
        if (requestLine == null) return false;
        return requestLine.contains("?");
    }

    public static String getFilePath(String requestLine) {
        return filePath(requestLine);
    }

    private static String filePath(String requestLine) {
        if (hasQuery(requestLine)) throw new IllegalArgumentException();
        if (requestLine == null) return DEFAULT_URL;

        String uri = getURI(requestLine);
        String path = uri.equals("/") ? DEFAULT_HTML_LOCATOR : uri;
        return BASE_FOLDER_NAME+path;
    }

    public static String getQuery(String requestLine) {
        if (requestLine == null || !hasQuery(requestLine)) throw new IllegalArgumentException();
        String uri = getURI(requestLine);
        return uri.substring(uri.indexOf("?")+1);
    }

    private static String getURI(String requestLine) {
        return requestLine.split(" ")[1];
    }
}
