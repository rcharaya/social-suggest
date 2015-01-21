package com.yahoo.hack.server.delivery;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yahoo.hack.infra.model.AnswerSuggestion;
import com.yahoo.hack.infra.model.CricketScoreSuggestion;
import com.yahoo.hack.infra.model.LinkSuggestion;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.infra.model.SuggestionDao;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.infra.model.UserDao;
import com.yahoo.hack.server.suggest.impl.AnswersSuggester;
import com.yahoo.hack.server.util.FacebookClient;
import com.yahoo.hack.server.util.ParamsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @since 10/12/11
 */
public class DeliveryDaemon {
    private Logger LOG = LoggerFactory.getLogger(DeliveryDaemon.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private SuggestionDao suggestionDao;

    public void run() {
        List<User> users = userDao.getAllUsers();
        LOG.info("crawling for : " + users.size() + " user(s)");
        for (User u : users) {
            List<Suggestion> suggestions = suggestionDao.getSuggestions(u);
            if (suggestions == null || suggestions.isEmpty()) {
                return;
            }

            for (Suggestion s : suggestions) {
                if ((s.isDelivered() != null && s.isDelivered())) {
                    continue;
                }
                s.setDelivered(true);
                s = suggestionDao.merge(s);

                if ((s instanceof LinkSuggestion)) {
                    postToFacebook(u, (LinkSuggestion) s);
                } else if((s instanceof CricketScoreSuggestion)) {
                    sendNotification(u, (CricketScoreSuggestion)s);
                } else if ((s instanceof AnswerSuggestion)) {
                    postToFacebook(u, (AnswerSuggestion) s);
                }

            }
        }
    }

    private void postToFacebook(User u, AnswerSuggestion s) {
        String data = "[Yahoo Answers] " + s.getSubject() + "\n\n" + s.getContent() + "\n\nSolution\n" + s.getSolution();

        postToFacebook(u, data);
    }

    private void sendNotification(User u, CricketScoreSuggestion s) {
        String str = s.getName() + "\n" + s.getScore() + "\n" + s.getVenue();
        IosNotifier.combined(str, 0, null);
        postToFacebook(u, str);
    }

    private void postToFacebook(User u, LinkSuggestion s) {
        String data = s.getTitle();
        data = (data == null ? "" : data.trim());
        data = data.replaceAll("<b>", " ");
        data = data.replaceAll("</b>", " ");
        data = data.replaceAll("\\s+", " ");

        data += " " + s.getUrl();

        postToFacebook(u, data);
    }

    private void postToFacebook(User u, String msg) {
        FacebookClient client = new FacebookClient(u.getFacebookAccessToken());
        client.setEndPoint("https://api.facebook.com/method/");

        JsonArray arr = new JsonArray();
        JsonObject el = new JsonObject();
        el.add("href", new JsonPrimitive("http://localhost:8080/site/"));
        el.add("text", new JsonPrimitive("Like Social Suggest"));
        arr.add(el);

        String actionLinks = arr.toString();

        String data = msg;
        data = (data == null ? "" : data.trim());

        try {
            client.call("stream.publish", ParamsHelper.toMap("message", data, "action_links", actionLinks));
        } catch (Exception e) {
            LOG.error("unable to publish post", e);
        }
    }

}
