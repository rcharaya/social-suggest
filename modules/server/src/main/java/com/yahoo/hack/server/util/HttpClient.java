package com.yahoo.hack.server.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @since 10/5/11
 */
public class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    public String call(String url) throws Exception {
        LOG.debug("calling url : {}", url);
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = conn.getInputStream();
        byte[] buff = new byte[1024];
        int n;
        while ((n = is.read(buff)) > 0) {
            bos.write(buff, 0, n);
        }
        is.close();
        bos.close();
        String data = bos.toString();
        //LOG.debug("data >> : {}", data);
        return data;
    }

    public String post(String url, Map<String, String> params) throws Exception {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> e : params.entrySet()) {
            str.append(e.getKey()).append(URLEncoder.encode(e.getValue())).append("&");
        }
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        connection.setRequestProperty("Content-Length", "" +
                Integer.toString(str.toString().getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream(
                connection.getOutputStream());
        wr.writeBytes(str.toString());
        wr.flush();
        wr.close();

        InputStream is = null;
        if (connection.getResponseCode() >= 400) {
            is = connection.getErrorStream();
        } else {
            is = connection.getInputStream();
        }

        //Get Response

        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    }

    public static void main(String... args) throws Exception {
        FacebookClient client = new FacebookClient("AAAD35or4v68BAAUex2riHcZBvHZBO4wA8XDlez6E2dZAg4I3TszBqXaceDMNJNs0FFYlZC5CiqKlK3A1iCGdkTzEZBr6L0k4ZD");
        client.setEndPoint("https://api.facebook.com/method/");


        JsonArray arr = new JsonArray();
        JsonObject el = new JsonObject();
        el.add("href", new JsonPrimitive("http://localhost:8080/site/"));
        el.add("text", new JsonPrimitive("Like Social Suggest"));
        arr.add(el);

        String actionLinks = arr.toString();

        client.call("stream.publish", ParamsHelper.toMap("message", "something asdasdaf http://www.yahoo.com/", "action_links", actionLinks));
    }


}
