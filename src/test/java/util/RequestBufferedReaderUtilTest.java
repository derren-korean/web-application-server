package util;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class RequestBufferedReaderUtilTest {

    private static String QUERY = "userId=aa&password=bb&name=cc&email=dd%40dd.dd";
    private static String HOST = "localhost:8080";

    String request;
    BufferedReader in;

    @Before
    public void setup() {
        request = "POST /user/create HTTP/1.1\n" +
                "Host: "+HOST+"\n" +
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
    public void 컨텐츠_길이_구하기() throws IOException {
        assertThat(46,is(RequestBufferedReaderUtil.getContentLength(in)));
    }

    @Test
    public void POST_쿼리() throws IOException {
        RequestBufferedReaderUtil.consumingBeforeQuery(in);
        assertThat(QUERY,is(in.readLine()));
    }

    @Test
    public void Host_구하기() throws IOException {
        String host = RequestBufferedReaderUtil.getHost(in);
        assertThat("http://"+HOST+"/",is(host));
    }
}