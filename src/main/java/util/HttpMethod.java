package util;

public enum HttpMethod {
    GET("GET"),
    POST("POST");

    public String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public boolean equals(String value) {
        return this.equals(valueOf(value));
    }
}
