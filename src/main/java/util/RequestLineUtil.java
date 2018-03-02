package util;

import java.net.HttpURLConnection;

public class RequestLineUtil {
    
    private static String BASE_FOLDER_NAME = "webapp";
    public static String DEFAULT_URL = BASE_FOLDER_NAME + "/" + HttpRequestUtils.DEFAULT_HTML_LOCATOR;
    
    public static boolean hasQuery(String requestLine) {
        if (requestLine == null) return false;
        if (HttpMethod.POST.equals(RequestLineUtil.httpMethodOf(requestLine))) return true;
        return requestLine.contains("?");
    }

    public static String getUri(String requestLine) {
        return uri(requestLine);
    }

    private static String uri(String requestLine) {
        if (hasQuery(requestLine)) throw new IllegalArgumentException();
        if (requestLine == null) return DEFAULT_URL;

        String uri = getURI(requestLine);
        String path = uri.equals("/") ? "/"+HttpRequestUtils.DEFAULT_HTML_LOCATOR : uri;
        return BASE_FOLDER_NAME+path;
    }

    public static String getQuery(String requestLine) {
        if (!hasQuery(requestLine)) throw new IllegalArgumentException();
        String uri = getURI(requestLine);
        return uri.substring(uri.indexOf("?")+1);
    }

    public static HttpMethod httpMethodOf(String requestLine) {
        return HttpMethod.valueOf(requestLine.split(" ")[0]);
    }

    private static String getURI(String requestLine) {
        return requestLine.split(" ")[1];
    }

    public static boolean containsURL(String requestLine) {
        return HttpMethod.GET.equals(httpMethodOf(requestLine)) && !getURI(requestLine).contains("?");
    }

    public static String statusCodeOf(String requestLine) {
        return HttpMethod.GET.equals(httpMethodOf(requestLine)) ? HttpURLConnection.HTTP_OK + " OK" : HttpURLConnection.HTTP_MOVED_TEMP + " Found";
    }
}
