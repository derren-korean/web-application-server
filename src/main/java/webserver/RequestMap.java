package webserver;

import com.google.common.collect.Maps;
import util.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class RequestMap {
    private static Map<String, String> request;

    public RequestMap(InputStream in) {
        if (in == null) throw new IllegalArgumentException();
        request = Maps.newHashMap();
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

    private void putQuery(BufferedReader bufferedReader) throws IOException {
        String query = null;
        if (HttpMethod.POST.equals(HttpMethod.valueOf(request.get("HttpMethod")))) {
            query = bufferedReader.readLine();
        }
        if (request.get("URI").contains("?")) {
            query = request.get("URI");
        }
        request.put("Query", query);
    }

    public String get(String key) {
        return request.get(key);
    }
}
