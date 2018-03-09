package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RequestMapTest {

    private static String QUERY = "userId=aa&password=bb&name=cc&email=dd%40dd.dd";

    String request;
    InputStream in;
    RequestMap requestMap;

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
        in = new ByteArrayInputStream(request.getBytes());
        requestMap = new RequestMap(in);
    }

    @Test
    public void 매쏘드() {
        assertThat("POST", is(requestMap.get("HttpMethod")));
    }

    @Test
    public void 쿼리() {
        assertThat(QUERY, is(requestMap.get("Query")));
    }

    @Test
    public void 로그인_쿠키() {
        assertThat(null, is(requestMap.get("Cookie")));
    }

    @Test
    public void 쿼리가_없는_경우() {
        request = "GET / HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7\n" +
                    "\n";
        in = new ByteArrayInputStream(request.getBytes());
        requestMap = new RequestMap(in);
        assertThat(null, is(requestMap.get("Query")));
    }
}