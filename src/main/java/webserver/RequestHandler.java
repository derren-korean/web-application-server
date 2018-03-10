package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestLineUtil;
import util.UserHandler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            ResponseHeaderStream.response(toBody(in, dos), dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] toBody(InputStream in, DataOutputStream out) throws IOException {
        return Files.readAllBytes(new File(response(new RequestMap(in), out)).toPath());
    }

    private String response(RequestMap in, DataOutputStream out) throws IOException {
        if (RequestLineUtil.contains(in,"/user/list")) {
            return userListResponse(in, out);
        }

        ResponseHeaderStream.setStatusCode(RequestLineUtil.StatusCodeOf(in), out);
        if (RequestLineUtil.hasUserQuery(in)) {
            return userQueryResponse(in, out);
        }

        return RequestLineUtil.responseOK(in);
    }

    private String userListResponse(RequestMap in, DataOutputStream out) throws IOException {
        ResponseHeaderStream.setStatusCode(UserHandler.StatusCodeOf(in),out);
        String uri = UserHandler.redirectByLoginCookie(in);

        if (uri.contains("login")) {
            ResponseHeaderStream.setRedirection(uri, out);
        }
        return uri;
    }

    private String userQueryResponse(RequestMap requestMap, DataOutputStream out) throws IOException {
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