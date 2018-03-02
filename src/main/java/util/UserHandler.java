package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class UserHandler {

    private static final Logger log = LoggerFactory.getLogger(UserHandler.class);

    public static User makeUser(String query) {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(query);
        return new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
    }

    public static String userResponse(String requestLine, BufferedReader requestReader, DataOutputStream out) throws IOException {
        if (RequestLineUtil.hasQuery(requestLine)) {
            String host = RequestBufferedReaderUtil.getHost(requestReader);
            User user = UserHandler.makeUser(getQuery(requestLine, requestReader, host, out));
            log.info("user Created" + user);
            return host+HttpRequestUtils.DEFAULT_HTML_LOCATOR;
        }
        return RequestLineUtil.getUri(requestLine);
    }

    public static String getQuery(String requestLine, BufferedReader requestReader, String host, DataOutputStream out) throws IOException {
        if (HttpMethod.POST.equals(RequestLineUtil.httpMethodOf(requestLine))) {
            out.writeBytes("Location: "+host+HttpRequestUtils.DEFAULT_HTML_LOCATOR+"\r\n");
            int contentLength = RequestBufferedReaderUtil.getContentLength(requestReader);
            RequestBufferedReaderUtil.consumingBeforeQuery(requestReader);
            return IOUtils.readData(requestReader, contentLength);
        }
        out.writeBytes("\r\n");
        return RequestLineUtil.getQuery(requestLine);
    }
}