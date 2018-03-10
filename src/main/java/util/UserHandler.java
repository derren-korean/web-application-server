package util;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestMap;

import java.net.HttpURLConnection;
import java.util.Map;

public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    public static String create(RequestMap requestMap) {
        return host(requestMap)+UserHandler.makeUser(requestMap.get("Query"));
    }

    private static String host(RequestMap requestMap) {
        return "http://"+requestMap.get("Host")+"/";
    }

    public static String login(RequestMap requestMap) {
        return host(requestMap)+login(requestMap.get("Query"));
    }

    private static String makeUser(String query) {
        if (query == null || query.isEmpty()) throw new IllegalArgumentException();
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(query);
        User user = new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
        DataBase.addUser(user);
        log.info("user Created" + user);
        return HttpRequestUtils.DEFAULT_HTML_LOCATOR;
    }

    private static String login(String query) {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(query);
        User user = DataBase.findUserById(userMap.get("userId"));
        if (user == null || !user.getPassword().equals(userMap.get("password"))) {
            return "user/login_failed.html";
        }
        return HttpRequestUtils.DEFAULT_HTML_LOCATOR;
    }

    public static String redirectByLoginCookie(RequestMap requestMap) {
        return requestMap.isLogined() ? RequestLineUtil.responseOK(requestMap) : "http://"+requestMap.get("Host") + "/user/login.html";
    }

    public static String StatusCodeOf(RequestMap requestMap) {
        return requestMap.isLogined() ? HttpURLConnection.HTTP_OK + " OK" : HttpURLConnection.HTTP_MOVED_TEMP + " Found";
    }
}