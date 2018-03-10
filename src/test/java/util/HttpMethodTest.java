package util;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HttpMethodTest {
    @Test
    public void 매쏘드_동일여부() {
        assertTrue(HttpMethod.POST.equals("POST"));
    }
}