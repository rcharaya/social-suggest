package com.yahoo.hack.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * @since 10/5/11
 */
public class YqlClient {

    private static final Logger LOG = LoggerFactory.getLogger(YqlClient.class);

    private boolean isSecure = false;
    private String endPoint = "://query.yahooapis.com/v1/public/yql?";
    private Format format = Format.JSON;
    private boolean diagnostics = false;
    private String store;

    public YqlClient() {
    }

    public String call(String query) throws Exception {

        query = URLEncoder.encode(query);

        StringBuilder str = new StringBuilder();
        str.append((isSecure ? "https" : "http") + endPoint);
        str.append("q=").append(query);
        str.append("&format=").append(format.name().toLowerCase());
        str.append("&diagnostics=").append(diagnostics);
        if (store != null) {
            str.append("&env=").append(URLEncoder.encode(store));
        }
        if (format == Format.JSON) {
            str.append("&callback=");
        }

        HttpClient client = new HttpClient();


        String data = client.call(str.toString());

        //LOG.debug("DATA > {}", data);
        return  data;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
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

    public boolean isDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(boolean diagnostics) {
        this.diagnostics = diagnostics;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
