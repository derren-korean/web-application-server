package webserver;

import util.HttpMethod;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestMap {
    private Map<String, String> request;

    public RequestMap(InputStream in) {
        if (in == null) throw new IllegalArgumentException();
        request = new HashMap<>();
        putAll(new BufferedReader(new InputStreamReader(in)));
    }

    private void putAll(BufferedReader bufferedReader) {
        try {
            putRequestLine(bufferedReader);
            putBeforeEmptyLine(bufferedReader);
            putQuery(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void putRequestLine(BufferedReader bufferedReader) throws IOException{
        String readLine = bufferedReader.readLine();
        String[] split = readLine.split(" ");
        request.put("HttpMethod", split[0]);
        request.put("URI", split[1]);
        request.put("Version", split[2]);
    }

    private void putBeforeEmptyLine(BufferedReader bufferedReader) throws IOException {
        String line = null;
        while(!(line = bufferedReader.readLine()).isEmpty()) {
            String[] split = line.split(": ");
            request.put(split[0], split[1]);
        }
    }

    private void putQuery(BufferedReader bufferedReader) {
        if (HttpMethod.POST.equals(request.get("HttpMethod"))) {
            request.put("Query", getPostBody(bufferedReader));
        }
        if (request.get("URI").contains("?")) {
            request.put("Query", request.get("URI").split("\\?")[1]);
        }
    }

    private String getPostBody(BufferedReader bufferedReader) {
        String query = null;
        try {
            query = IOUtils.readData(bufferedReader, Integer.valueOf(request.get("Content-Length")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return query;
    }

    public String get(String key) {
        return request.get(key);
    }

    public boolean isLogined() {
        return request.get("Cookie") != null && Boolean.valueOf(request.get("Cookie").split("=")[1]);
    }
}
