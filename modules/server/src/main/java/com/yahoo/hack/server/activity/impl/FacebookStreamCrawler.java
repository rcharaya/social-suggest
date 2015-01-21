package com.yahoo.hack.server.activity.impl;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.Device;
import com.yahoo.hack.infra.model.FacebookActivity;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.server.activity.ActivityCrawler;
import com.yahoo.hack.server.util.FacebookClient;
import com.yahoo.hack.server.util.ParamsHelper;
import com.yahoo.hack.server.util.facebook.HomeFeed;
import com.yahoo.hack.server.util.facebook.HomeFeedEntry;
import com.yahoo.hack.server.util.facebook.HomeFeedEntryFrom;
import com.yahoo.hack.server.util.facebook.HomeFeedEntryLikes;

/**
 * @since 10/5/11
 */
public class FacebookStreamCrawler implements ActivityCrawler {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    private static final Logger LOG = LoggerFactory
            .getLogger(FacebookStreamCrawler.class);

    public String getName() {
        return "facebook_stream";
    }

    public List<Activity> crawl(User user, long lastCrawled) {

        boolean postedByUser;
        boolean likedByUser;

        if (user == null) {
            return null;
        }

        if (user.getFacebookAccessToken() == null) {
            return null;
        }

        List<Activity> activities = new ArrayList<Activity>();

        try {
            FacebookClient client = new FacebookClient(user
                    .getFacebookAccessToken());

            String userId = getUserId(client);

            String data = client.call("me/feed", ParamsHelper
                    .toMap());

            Gson gson = new Gson();
            java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<HomeFeed>() {
            }.getType();
            HomeFeed homeFeed = gson.fromJson(new StringReader(data), listType);
            for (HomeFeedEntry e : homeFeed.getData()) {
                postedByUser = false;
                likedByUser = false;
                HomeFeedEntryFrom from = e.getFrom();
                if (userId.equals(from.getId())) {
                    postedByUser = true;
                } else {
                    HomeFeedEntryLikes likes = e.getLikes();
                    if (likes != null && likes.getCount() != 0) {
                        List<HomeFeedEntryFrom> likesData = likes.getData();
                        for (HomeFeedEntryFrom d : likesData) {
                            if (userId.equals(d.getId())) {
                                likedByUser = true;
                                break;
                            }
                        }
                    }
                }
                if (postedByUser || likedByUser) {
                    FacebookActivity a = new FacebookActivity();
                    a.setTimeStamp(DATE_FORMAT.parse(e.getCreated_time()).getTime());
                    if (a.getTimeStamp() < lastCrawled) {
                        continue;
                    }
                    String app = e.getApplication() == null ? null : e.getApplication().getName();
                    app = (app == null ? "" : app.toLowerCase().trim());
                    if (app.contains("socialsuggest")) {
                        continue;
                    }
                    if (app.matches(".*mobile.*")
                            || app.matches(
                            ".*samsung.*")
                            || app.matches(
                            ".*iphone.*")) {
                        a.setDevice(Device.MOBILE);
                    } else {
                        a.setDevice(Device.DESKTOP);
                    }
                    a.setCaption(e.getCaption());
                    a.setDescription(e.getDescription());
                    a.setMessage(e.getMessage());
                    a.setName(e.getName());
                    a.setStory(e.getStory());

                    String link = e.getLink();
                    link = (link == null ? "" : link.trim());
                    if (link.length() == 0 || link.startsWith("http://www.facebook.com/") || link.startsWith("https://www.facebook.com")) {
                        // else
                    } else {
                        a.setLink(link);
                    }

                    activities.add(a);
                }

            }
        } catch (Exception e) {
            LOG.error("unable to parse facebook feed", e);
        }
        return activities;
    }

    private String getUserId(FacebookClient client) throws Exception {
        String data = client.call("me", ParamsHelper.toMap());
        JsonParser parser = new JsonParser();
        return parser.parse(data).getAsJsonObject().get("id").getAsString();
    }
}
