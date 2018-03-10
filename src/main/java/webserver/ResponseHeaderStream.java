package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHeaderStream {

    private static final Logger log = LoggerFactory.getLogger(ResponseHeaderStream.class);

    public static void response(byte[] body, DataOutputStream dos) {
        responseHeader(dos, body.length);
        responseBody(dos, body);
    }

    public static void setRedirection(String uri, DataOutputStream out) throws IOException {
        out.writeBytes("Location: "+uri+"\r\n");
    }

    public static void setStatusCode(String statusCode, DataOutputStream out) throws IOException {
        out.writeBytes("HTTP/1.1 "+statusCode+"\r\n");
    }

    public static void setLoginCookie(boolean loginResult, DataOutputStream out) throws IOException {
        out.writeBytes("Set-Cookie: logined="+loginResult+"\r\n");
    }

    static void responseHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
