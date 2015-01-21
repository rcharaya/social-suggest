package com.yahoo.hack.server.util;

import java.net.URLEncoder;
import java.util.Map;

/**
 * @since 10/5/11
 */
public class FacebookClient {

    private String endPoint = "https://graph.facebook.com/";
    private Format format = Format.JSON;

    private String accessToken;

    public FacebookClient(String accessToken) {
        this.accessToken = accessToken;
    }

    public String call(String method, Map<String, String> params) throws Exception {
        StringBuilder str = new StringBuilder();
        str.append(endPoint);
        str.append(method).append("?");
        str.append("access_token=").append(accessToken);
        str.append("&format=").append(format.name().toLowerCase());
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                str.append("&").append(URLEncoder.encode(e.getKey())).append("=").append(URLEncoder.encode(e.getValue()));
            }
        }

        HttpClient client = new HttpClient();
        return client.call(str.toString());
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
