package webserver;

import org.junit.Before;
import org.junit.Test;
import util.HttpMethod;

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
                "Content-Length: 46\n" +
                "\n" +
                QUERY;
        requestMap = initRequestMap(request);
    }

    @Test
    public void 매쏘드() {
        assertThat("POST", is(requestMap.get("HttpMethod")));
        assertThat(true, is(HttpMethod.POST.equals(requestMap.get("HttpMethod"))));
        assertThat(HttpMethod.POST, is(HttpMethod.valueOf(requestMap.get("HttpMethod"))));
    }

    @Test
    public void 쿼리() {
        assertThat(QUERY, is(requestMap.get("Query")));
    }

    @Test
    public void 로그인_쿠키() {
        assertThat(null, is(requestMap.get("Cookie")));
        assertThat(false, is(requestMap.isLogined()));

        String request = "GET / HTTP/1.1\n" +
        "Cookie: logined=true\n" + "\n";

        assertThat(true, is(initRequestMap(request).isLogined()));
    }

    @Test
    public void 쿼리가_없는_경우() {
        request = "GET / HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "\n";
        assertThat(null, is(initRequestMap(request).get("Query")));
    }

    private RequestMap initRequestMap(String request) {
        return new RequestMap(new ByteArrayInputStream(request.getBytes()));
    }
}