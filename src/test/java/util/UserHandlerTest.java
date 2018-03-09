package util;

import db.DataBase;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
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
        new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
    }

    @Test
    public void 쿼리_얻기() throws IOException {
        String requestLine = in.readLine();
        DataOutputStream out = new DataOutputStream(new FileOutputStream("test"));

        assertThat(QUERY, is(UserHandler.getQuery(requestLine, in)));
        out.flush();
    }

    @Test
    public void 사용자_생성() throws IOException {
        String requestLine = in.readLine();
        String url = "http://localhost:8080/" + HttpRequestUtils.DEFAULT_HTML_LOCATOR;
        assertThat(url, is(UserHandler.create(requestLine, in)));
    }

    @Test
    public void 로그인_실패() throws IOException {
        String query = "userId=aa&password=cc";
        request = "POST /user/login HTTP/1.1\n" +
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
                query;
        in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));

        String requestLine = in.readLine();
        String url = "http://localhost:8080/" + "user/login_failed.html";
        assertThat(url, is(UserHandler.login(requestLine, in)));
    }

    @Test
    public void 로그인_성공() throws IOException {
        String requestLine = in.readLine();
        String url = "http://localhost:8080/" + HttpRequestUtils.DEFAULT_HTML_LOCATOR;
        assertThat(url, is(UserHandler.create(requestLine, in)));

        String query = "userId=aa&password=bb";
        request = "POST /user/login HTTP/1.1\n" +
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
                query;
        in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));

        requestLine = in.readLine();
        assertThat(url, is(UserHandler.login(requestLine, in)));
    }
}