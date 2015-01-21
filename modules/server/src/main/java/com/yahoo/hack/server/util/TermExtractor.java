package com.yahoo.hack.server.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 10/11/11
 */
public class TermExtractor {

    private static final Logger LOG = LoggerFactory.getLogger(TermExtractor.class);

    public List<String> extract(String text) {
        List<String> list = new ArrayList<String>();
        text = (text == null ? "" : text);
        text = text.replaceAll("'", " ");
        text = text.replaceAll("\"", " ");
        text = text.replaceAll("\\s+", " ");
        text = text.trim().toLowerCase();

        if (text.length() == 0) {
            return list;
        }

        String query = "select * from search.termextract where context=\"" + text + "\"";
        YqlClient client = new YqlClient();
        try {
            String data = client.call(query);
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(data).getAsJsonObject();
            root = root.get("query").getAsJsonObject();
            JsonElement results = root.get("results");
            if (results == null || results.isJsonNull()) {
                return list;
            }
            results = results.getAsJsonObject().get("Result");

            if (results instanceof JsonArray) {
                JsonArray arr = results.getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    String term = normalize(arr.get(i).getAsString());
                    if (term.length() == 0) {
                        continue;
                    }
                    list.add(term);
                }
            } else if (results instanceof JsonPrimitive) {
                String term = normalize(results.getAsString());
                if (term.length() > 0) {
                    list.add(term);
                }
            }
        } catch (Exception e) {
            LOG.error("error occured while extracting terms", e);
        }

        return list;
    }

    private String normalize(String term) {
        term = (term == null ? "" : term.trim().toLowerCase());
        term = term.replaceAll("\\s+", " ");
        return term;
    }

}
