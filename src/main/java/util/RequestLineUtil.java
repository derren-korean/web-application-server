package util;

public class RequestLineUtil {
    
    private static String BASE_FOLDER_NAME = "webapp";
    private static String DEFAULT_HTML_LOCATOR = "/index.html";
    public static String DEFAULT_URL = BASE_FOLDER_NAME + DEFAULT_HTML_LOCATOR;
    
    public static boolean hasQuery(String requestLine) {
        if (requestLine == null) return false;
        if (HttpMethod.POST.equals(RequestLineUtil.httpMethod(requestLine))) return true;
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
        if (!hasQuery(requestLine)) throw new IllegalArgumentException();
        String uri = getURI(requestLine);
        return uri.substring(uri.indexOf("?")+1);
    }

    private static String getURI(String requestLine) {
        return requestLine.split(" ")[1];
    }

    public static HttpMethod httpMethod(String requestLine) {
        return HttpMethod.valueOf(requestLine.split(" ")[0]);
    }

    public static boolean containsURL(String requestLine) {
        return HttpMethod.GET.equals(httpMethod(requestLine)) && !getURI(requestLine).contains("?");
    }
}
