package com.yahoo.hack.server.activity;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.User;

import java.util.List;

/**
 * @since 10/1/11
 */
public interface ActivityCrawler {

    public String getName();

    List<Activity> crawl(User user, long lastCrawled);

}
