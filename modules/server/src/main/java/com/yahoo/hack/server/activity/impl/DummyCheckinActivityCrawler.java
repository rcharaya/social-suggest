package com.yahoo.hack.server.activity.impl;

import com.yahoo.hack.infra.model.TweetActivity;
import com.yahoo.hack.server.activity.ActivityCrawler;
import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.CheckinActivity;
import com.yahoo.hack.infra.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * @since 10/2/11
 */
public class DummyCheckinActivityCrawler implements ActivityCrawler {
    public String getName() {
        return "dummy";
    }

    public List<Activity> crawl(User user, long lastCrawled) {
        TweetActivity activity = new TweetActivity();
        activity.setStatus("something :)");
        activity.setTimeStamp(System.currentTimeMillis());

        return Arrays.asList(new Activity[]{activity});
    }
}
