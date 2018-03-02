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
            byte[] body = toBody(in, dos);
            responseHeader(dos, body.length);
            responseBody(dos, body);
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
        String uri = null;
        out.writeBytes("HTTP/1.1 "+RequestLineUtil.statusCodeOf(requestLine)+" \r\n");
        if (RequestLineUtil.containsURL(requestLine)) {
            uri = RequestLineUtil.getUri(requestLine);
        }
        if (requestLine.contains("user")) {
            uri = UserHandler.userResponse(requestLine, requestReader, out);
            out.writeBytes( uri + "\r\n");
        }
        return uri;
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}