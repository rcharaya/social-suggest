package com.yahoo.hack.server.suggest.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.hack.infra.model.AnswerSuggestion;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.server.suggest.Suggester;
import com.yahoo.hack.server.util.YqlClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @since 10/16/11
 */
public class AnswersSuggester implements Suggester {

    private static final Logger LOG = LoggerFactory.getLogger(AnswersSuggester.class);

    public String getCategory() {
        return "answers";
    }

    public List<Suggestion> suggest(Set<String> categories, Set<String> terms, Set<String> queries) {
        if (terms == null || terms.isEmpty()) {
            return null;
        }
        YqlClient client = new YqlClient();

        String t = "";
        for (String s : terms) {
            t += " " + s + " ";
        }

        String q = "select * from answers.search where query=\"" + t + "\" and type=\"resolved\"";


        List<Suggestion> suggestions = new ArrayList<Suggestion>();

        try {
            String data = client.call(q);
            LOG.debug("data >> {}", data);
            JsonObject root = new JsonParser().parse(data).getAsJsonObject().get("query").getAsJsonObject().get("results").getAsJsonObject();
            JsonElement el = root.get("Question");
            if (el instanceof JsonObject) {
                JsonObject value = el.getAsJsonObject();
                AnswerSuggestion suggestion = newSuggestion(value);
                suggestions.add(suggestion);
            } else if (el instanceof JsonArray) {
                JsonArray arr = el.getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject value = arr.get(i).getAsJsonObject();
                    AnswerSuggestion suggestion = newSuggestion(value);
                    suggestions.add(suggestion);
                    break;
                }
            }
        } catch (Exception e) {
            LOG.error("exception occured", e);
        }
        return suggestions;
    }

    private AnswerSuggestion newSuggestion(JsonObject value) {
        AnswerSuggestion suggestion = new AnswerSuggestion();
        suggestion.setSubject(value.get("Subject").getAsString());
        suggestion.setContent(value.get("Content").getAsString());
        if (value.has("ChosenAnswer")) {
            suggestion.setSolution(value.get("ChosenAnswer").getAsString());
        }
        return suggestion;
    }
}
