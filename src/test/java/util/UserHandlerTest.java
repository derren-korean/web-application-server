package util;

import model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class UserHandlerTest {

    private static String QUERY = "userId=aa&password=bb&name=cc&email=dd%40dd.dd";

    String request;
    BufferedReader in;

    @Before
    public void setup() {
        request = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 46\n" +
                "Cache-Control: max-age=0\n" +
                "Origin: http://localhost:8080\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                "Referer: http://localhost:8080/user/form.html\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
                "\n" +
                QUERY;

        in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
    }

    @Test
    public void queryParsing() {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(QUERY);
        User user = new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
        System.out.println(user);
    }

    @Test
    public void 컨텐츠_길이_구하기() throws IOException {
        assertThat(46,is(UserHandler.getContentLength(in)));
    }

    @Test
    public void 쿼리_얻기() throws IOException {
        String requestLine = in.readLine();

        assertThat(QUERY, is(UserHandler.getQuery(requestLine, in)));
    }
}