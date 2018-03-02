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
        if (RequestLineUtil.hasQuery(requestLine)) {
            User user = UserHandler.makeUser(getQuery(requestLine, requestReader));
            log.info("user Created" + user);
            return RequestLineUtil.DEFAULT_URL;
        }
        return RequestLineUtil.getFilePath(requestLine);
    }

    public static String getQuery(String requestLine, BufferedReader requestReader) throws IOException {
        if (HttpMethod.POST.equals(RequestLineUtil.httpMethod(requestLine))) {
            int contentLength = getContentLength(requestReader);
            consumingBeforeQuery(requestReader);
            return IOUtils.readData(requestReader, contentLength);
        }
        return RequestLineUtil.getQuery(requestLine);
    }

    public static int getContentLength(BufferedReader requestReader) throws IOException {
        String line = null;
        while(true){
            line = requestReader.readLine();
            if (line.contains("Content-Length")) {
                break;
            }
        }
        return Integer.valueOf(line.split(" ")[1]);
    }

    private static void consumingBeforeQuery(BufferedReader requestReader) throws IOException {
        while(!requestReader.readLine().isEmpty()){
        }
    }

}
