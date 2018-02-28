package util;

import model.User;

import java.util.Map;

public class UserHandler {
    public static User makeUser(String query) {
        Map<String, String> userMap = HttpRequestUtils.parseQueryString(query);
        return new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
    }
}
