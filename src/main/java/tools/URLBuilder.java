package tools;

public class URLBuilder {

    public static URLBuilder create(String baseURL) {
        return new URLBuilder(baseURL);
    }

    private StringBuilder urlBuilder;

    private URLBuilder(String baseURL) {
        this.urlBuilder = new StringBuilder((baseURL));
    }

    public URLBuilder addParam(String key, String value) {
        String param = value.replaceAll("\\s{2,}", " ").trim().replaceAll(" ", "+");
        urlBuilder.append("&").append(key).append("=").append(param);
        return this;
    }

    public String build() {
        return urlBuilder.toString();
    }
}

