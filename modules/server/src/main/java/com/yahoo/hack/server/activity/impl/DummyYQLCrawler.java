package com.yahoo.hack.server.activity.impl;

import com.yahoo.hack.server.activity.ActivityCrawler;
import com.yahoo.hack.server.util.YqlClient;
import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @since 10/5/11
 */
public class DummyYQLCrawler implements ActivityCrawler {

    private static final Logger LOG = LoggerFactory.getLogger(DummyYQLCrawler.class);

    public String getName() {
        return "dummy_yql";
    }

    public List<Activity> crawl(User user, long lastCrawled) {
        try {
            String query = "SELECT * FROM facebook.graph WHERE id='ritwik.saikia'";
            YqlClient yqlClient = new YqlClient();
            yqlClient.setStore("store://datatables.org/alltableswithkeys");
            String out = yqlClient.call(query);

            LOG.info("YqlClient output> " + out);
        } catch (Exception e) {
            // only log exception
        }
        return null;
    }
}
