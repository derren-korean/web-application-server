package util;

import model.User;
import org.junit.Before;
import org.junit.Test;
import webserver.RequestMap;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UserHandlerTest {

    private static String QUERY = "userId=aa&password=bb&name=cc&email=dd%40dd.dd";

    String request;
    InputStream in;
    RequestMap requestMap;

    @Before
    public void setup() {
        request = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Content-Length: 46\n" +
                "\n" +
                QUERY;
        requestMap = initRequestMap(request);
    }

    @Test
    public void queryParsing() {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(QUERY);
        new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
    }

    @Test
    public void 사용자_생성() {
        requestMap = initRequestMap(request);
        String url = "http://localhost:8080/" + HttpRequestUtils.DEFAULT_HTML_LOCATOR;
        assertThat(url, is(UserHandler.create(requestMap)));
    }

    @Test
    public void 로그인_실패() {
        requestMap = initRequestMap(loginRequest("userId=aa&password=cc"));
        String url = "http://localhost:8080/" + "user/login_failed.html";
        assertThat(url, is(UserHandler.login(requestMap)));
    }

    @Test
    public void 로그인_성공__생성후_로그인() {
        String url = "http://localhost:8080/" + HttpRequestUtils.DEFAULT_HTML_LOCATOR;
        assertThat(url, is(UserHandler.create(requestMap)));

        requestMap = initRequestMap(loginRequest("userId=aa&password=bb"));
        assertThat(url, is(UserHandler.login(requestMap)));
    }

    private String loginRequest(String query) {
        return "POST /user/login HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Content-Length: 46\n" +
                "\n" +
                query;
    }

    @Test
    public void 쿠키확인_리다이렉션() {
        requestMap = initRequestMap(listRequest(""));
        assertThat("http://localhost:8080/user/login.html", is(UserHandler.redirectByLoginCookie(requestMap)));

        requestMap = initRequestMap(listRequest("Cookie: logined=true\n"));
        assertThat("webapp/user/list.html", is(UserHandler.redirectByLoginCookie(requestMap)));
    }

    private String listRequest(String cookieLine) {
        return "GET /user/list.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Content-Length: 46\n" +
                cookieLine +
                "\n";
    }

    @Test
    public void 상태코드() {
        assertThat("302 Found", is(UserHandler.StatusCodeOf(requestMap)));

        requestMap = initRequestMap(listRequest("Cookie: logined=true\n"));
        assertThat("200 OK", is(UserHandler.StatusCodeOf(requestMap)));
    }

    private RequestMap initRequestMap(String request) {
        return new RequestMap(new ByteArrayInputStream(request.getBytes()));
    }
}