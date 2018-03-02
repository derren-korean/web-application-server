package util;

import org.junit.Test;

import java.io.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RequestLineUtilTest {

    @Test
    public void 파일_경로() {
        String caseOnlyUrlLine = "GET /index.html HTTP/1.1";
        assertThat("webapp/index.html", is(RequestLineUtil.getUri(caseOnlyUrlLine)));
    }

    @Test (expected = IllegalArgumentException.class)
    public void 파일_경로_쿼리_일경우() {
        String caseQueryLine = "GET /user/create?userId=aa&password=bb&name=cc&email=dd%40dd.dd HTTP/1.1";
        assertThat("webapp/index.html", is(RequestLineUtil.getUri(caseQueryLine)));
    }

    @Test
    public void 쿼리_추출() {
        String caseQueryLine = "GET /user/create?userId=aa&password=bb&name=cc&email=dd%40dd.dd HTTP/1.1";
        assertThat("userId=aa&password=bb&name=cc&email=dd%40dd.dd", is(RequestLineUtil.getQuery(caseQueryLine)));
    }

    @Test
    public void http_매쏘드_이름_비교() throws IOException {
        String request = "GET /user/form.html HTTP/1.1";

        BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(request.getBytes())));
        String requestLine = in.readLine();

        assertThat(HttpMethod.GET, is(RequestLineUtil.httpMethodOf(requestLine)));
    }

    @Test
    public void request_line_URL_포함() {
        assertThat(true, is(RequestLineUtil.containsURL("GET /user/form.html HTTP/1.1")));
        assertThat(false, is(RequestLineUtil.containsURL("GET /user/create?userId=aa&password=bb&name=cc&email=dd%40dd.dd HTTP/1.1")));
    }

    @Test
    public void 상태_코드_매쏘드_별() {
        assertThat("200 OK", is(RequestLineUtil.statusCodeOf("GET /user/form.html HTTP/1.1")));
        assertThat("302 Found", is(RequestLineUtil.statusCodeOf("POST /user/create HTTP/1.1")));
    }
}