package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    public static User makeUser(String query) {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(query);
        return new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
    }

    public static String userResponse(String requestLine, BufferedReader requestReader) throws IOException {
        String host = RequestBufferedReaderUtil.getHost(requestReader);

        User user = UserHandler.makeUser(getQuery(requestLine, requestReader));
        log.info("user Created" + user);
        return host+HttpRequestUtils.DEFAULT_HTML_LOCATOR;
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