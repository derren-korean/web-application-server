package util;

import org.junit.Before;
import org.junit.Test;
import webserver.RequestMap;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RequestLineUtilTest {

    private static String QUERY = "userId=aa&password=bb&name=cc&email=dd%40dd.dd";

    String request;
    InputStream in;
    RequestMap requestMap;

    @Before
    public void setup() {
        request = "POST /user/create HTTP/1.1\n" +
                "Content-Length: 46\n" +
                "\n" +
                QUERY;
    }

    @Test
    public void 상태코드() {
        requestMap = initRequestMap(request);
        assertThat(HttpURLConnection.HTTP_MOVED_TEMP + " Found", is(RequestLineUtil.StatusCodeOf(requestMap)));

        requestMap = initRequestMap("GET user/create?"+QUERY+" HTTP/1.1\n" + "\n");
        assertThat(HttpURLConnection.HTTP_MOVED_TEMP + " Found", is(RequestLineUtil.StatusCodeOf(requestMap)));

        requestMap = initRequestMap("GET user/create HTTP/1.1\n" + "\n");
        assertThat(HttpURLConnection.HTTP_OK + " OK", is(RequestLineUtil.StatusCodeOf(requestMap)));
    }

    private RequestMap initRequestMap(String request) {
        return new RequestMap(new ByteArrayInputStream(request.getBytes()));
    }
}