package util;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    public static String create(String requestLine, BufferedReader requestReader) throws IOException {
        return RequestBufferedReaderUtil.getHost(requestReader)+UserHandler.makeUser(getQuery(requestLine, requestReader));
    }

    public static String login(String requestLine, BufferedReader requestReader) throws IOException {
        return RequestBufferedReaderUtil.getHost(requestReader)+login(getQuery(requestLine, requestReader));
    }

    private static String makeUser(String query) {
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

    public static String getQuery(String requestLine, BufferedReader requestReader) throws IOException {
        if (HttpMethod.POST.equals(RequestLineUtil.httpMethodOf(requestLine))) {
            int contentLength = RequestBufferedReaderUtil.getContentLength(requestReader);
            RequestBufferedReaderUtil.consumingBeforeQuery(requestReader);

            return IOUtils.readData(requestReader, contentLength);
        }
        return RequestLineUtil.getQuery(requestLine);
    }
}