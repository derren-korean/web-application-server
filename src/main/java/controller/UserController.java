package controller;

import util.RequestLineUtil;
import util.UserHandler;
import webserver.RequestMap;
import webserver.ResponseHeaderStream;

import java.io.DataOutputStream;
import java.io.IOException;

public class UserController {

    public static String userResponse(RequestMap in, DataOutputStream out) throws IOException {
        if (RequestLineUtil.contains(in,"/user/list")) {
            return userListResponse(in, out);
        }

        ResponseHeaderStream.setStatusCode(RequestLineUtil.StatusCodeOf(in), out);
        if (RequestLineUtil.hasUserQuery(in)) {
            return userQueryResponse(in, out);
        }
        return RequestLineUtil.responseOK(in);
    }

    private static String userListResponse(RequestMap in, DataOutputStream out) throws IOException {
        ResponseHeaderStream.setStatusCode(UserHandler.StatusCodeOf(in),out);
        String uri = UserHandler.redirectByLoginCookie(in);

        if (uri.contains("login")) {
            ResponseHeaderStream.setRedirection(uri, out);
        }
        return uri;
    }

    private static String userQueryResponse(RequestMap requestMap, DataOutputStream out) throws IOException {
        String uri = null;
        if (RequestLineUtil.contains(requestMap,"create")) {
            uri = UserHandler.create(requestMap);
        }
        if (RequestLineUtil.contains(requestMap, "login")) {
            uri = UserHandler.login(requestMap);
            ResponseHeaderStream.setLoginCookie(!uri.contains("login_failed"), out);
        }
        ResponseHeaderStream.setRedirection(uri, out);
        return uri;
    }
}
