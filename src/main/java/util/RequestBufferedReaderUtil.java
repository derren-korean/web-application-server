package util;

import java.io.BufferedReader;
import java.io.IOException;

class RequestBufferedReaderUtil {

    static int getContentLength(BufferedReader requestReader) throws IOException {
        return Integer.valueOf(getLine(requestReader, "Content-Length").split(" ")[1]);
    }

    static void consumingBeforeQuery(BufferedReader requestReader) throws IOException {
        while(!requestReader.readLine().isEmpty());
    }

    static String getHost(BufferedReader requestReader) throws IOException {
        return "http://"+ getLine(requestReader, "Host").split(" ")[1]+"/";
    }

    private static String getLine(BufferedReader requestReader, String title) throws IOException {
        String line = null;
        while(!(line = requestReader.readLine()).contains(title)){
        }
        return line;
    }
}