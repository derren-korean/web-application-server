package util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RequestLineUtilTest {

    @Test
    public void 파일_경로() {
        String caseOnlyUrlLine = "GET /index.html HTTP/1.1";
        assertThat("webapp/index.html", is(RequestLineUtil.getFilePath(caseOnlyUrlLine)));
    }

    @Test (expected = IllegalArgumentException.class)
    public void 파일_경로_쿼리_일경우() {
        String caseQueryLine = "GET /user/create?userId=aa&password=bb&name=cc&email=dd%40dd.dd HTTP/1.1";
        assertThat("webapp/index.html", is(RequestLineUtil.getFilePath(caseQueryLine)));
    }

    @Test
    public void 쿼리_추출() {
        String caseQueryLine = "GET /user/create?userId=aa&password=bb&name=cc&email=dd%40dd.dd HTTP/1.1";
        assertThat("userId=aa&password=bb&name=cc&email=dd%40dd.dd", is(RequestLineUtil.getQuery(caseQueryLine)));
    }
}