package com.yahoo.hack.server.activity.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.hack.infra.config.Credentials;
import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.TweetActivity;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.server.activity.ActivityCrawler;
import com.yahoo.hack.server.util.YqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @since 10/5/11
 */
public class TwitterStatusCrawler implements ActivityCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterStatusCrawler.class);

    public String getName() {
        return "twitter_status";
    }

    public List<Activity> crawl(User user, long lastCrawled) {
        if (user == null) {
            return null;
        }

        if (user.getTwitterAccessToken() == null) {
            return null;
        }
        String query = "select * from twitter.status.timeline.user where oauth_consumer_key='" + Credentials.getProperty(Credentials.TWITTER_APP_ID)
                + "' and oauth_consumer_secret='" + Credentials.getProperty(Credentials.TWITTER_APP_SECRET)
                + "' and oauth_token='" + user.getTwitterAccessToken()
                + "' and oauth_token_secret='" + user.getTwitterAccessTokenSecret() + "'";

        try {
            YqlClient client = new YqlClient();
            client.setStore("store://datatables.org/alltableswithkeys");
            client.setSecure(true);
            String data = client.call(query);

            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(data).getAsJsonObject();

            JsonObject results = root.getAsJsonObject("query").getAsJsonObject("results");
            JsonArray statuses = results.getAsJsonObject("statuses").getAsJsonArray("status");
            List<Activity> activities = new ArrayList<Activity>();
            for (int i = 0; i < statuses.size(); i++) {
                JsonObject row = statuses.get(i).getAsJsonObject();
                long createdAt = new Date(row.getAsJsonPrimitive("created_at").getAsString()).getTime();
                if (createdAt <= lastCrawled) {
                    continue;
                }

                TweetActivity a = new TweetActivity();
                a.setStatus(row.getAsJsonPrimitive("text").getAsString());
                a.setTimeStamp(createdAt);
                activities.add(a);
            }

            return activities;

        } catch (Exception e) {
           LOG.warn("exception occured : ", e);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
