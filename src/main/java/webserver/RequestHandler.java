package webserver;

import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestLineUtil;
import util.ResponseHeaderSession;
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
            ResponseHeaderSession.init();
            ResponseHeaderStream.response(toBody(in, dos), dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] toBody(InputStream in, DataOutputStream out) throws IOException {
        return Files.readAllBytes(new File(response(new RequestMap(in), out)).toPath());
    }

    private String response(RequestMap in, DataOutputStream out) throws IOException {
        if (in.get("Accept").contains("css")) {
            ResponseHeaderSession.setContentType("text/css");
        }
        if (RequestLineUtil.contains(in,"/user")) {
            return UserController.userResponse(in, out);
        }

        ResponseHeaderStream.setStatusCode(UserHandler.StatusCodeOf(in),out);
        return RequestLineUtil.responseOK(in);
    }
}