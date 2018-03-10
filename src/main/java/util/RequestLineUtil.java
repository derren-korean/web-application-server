package util;

import webserver.RequestMap;

import java.net.HttpURLConnection;

public class RequestLineUtil {
    
    private static String BASE_FOLDER_NAME = "webapp";

    public static boolean hasQuery(RequestMap requestMap) {
        if (HttpMethod.POST.equals(requestMap.get("HttpMethod"))) return true;
        return contains(requestMap, "?");
    }

    public static String responseOK(RequestMap requestMap) {
        if (hasQuery(requestMap)) throw new IllegalArgumentException();

        String uri = requestMap.get("URI");
        String path = uri.equals("/") ? "/"+HttpRequestUtils.DEFAULT_HTML_LOCATOR : uri;
        return BASE_FOLDER_NAME+path;
    }

    public static boolean containsURL(RequestMap requestMap) {
        return HttpMethod.GET.equals(requestMap.get("HttpMethod")) && !requestMap.get("URI").contains("?");
    }

    public static String StatusCodeOf(RequestMap requestMap) {
        return containsURL(requestMap) ? HttpURLConnection.HTTP_OK + " OK" : HttpURLConnection.HTTP_MOVED_TEMP + " Found";
    }

    public static boolean hasUserQuery(RequestMap requestMap) {
        return contains(requestMap,"user") && hasQuery(requestMap);
    }

    public static boolean contains(RequestMap requestMap, String string) {
        return requestMap.get("URI").contains(string);
    }
}
