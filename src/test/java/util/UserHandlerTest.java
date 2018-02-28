package util;

import model.User;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class UserHandlerTest {

    @Test
    public void queryParsing() {
        String query = "userId=aa&password=bb&name=cc&email=dd%40dd.dd";
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(query);
        User user = new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
        System.out.println(user);
    }
}