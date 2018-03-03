package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestLineUtil;
import util.UserHandler;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;

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
        return Files.readAllBytes(new File(response(in, out)).toPath());
    }

    private String response(InputStream in, DataOutputStream out) throws IOException {
        BufferedReader requestReader = new BufferedReader(new InputStreamReader(in));
        String requestLine = requestReader.readLine();
        ResponseHeaderStream.setStatusCode(RequestLineUtil.statusCodeOf(requestLine), out);

        if (RequestLineUtil.hasUserQuery(requestLine)) {
            String uri = userResponse(requestLine, requestReader, out);
            ResponseHeaderStream.setRedirection(uri, out);
            return uri;
        }
        return RequestLineUtil.getUri(requestLine);
    }

    private String userResponse(String requestLine, BufferedReader requestReader, DataOutputStream out) throws IOException {
        String uri = null;
        if (requestLine.contains("create")) {
            uri = UserHandler.create(requestLine, requestReader);
        }
        if (requestLine.contains("login")) {
            uri = UserHandler.login(requestLine, requestReader);
            ResponseHeaderStream.setLoginCookie(!uri.contains("login_failed"), out);
        }
        return uri;
    }
}