package com.yahoo.hack.server.activity;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.ActivityDao;
import com.yahoo.hack.infra.model.CrawlInfo;
import com.yahoo.hack.infra.model.CrawlerDao;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.infra.model.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 10/1/11
 */
public class ActivityCrawlerDaemon {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityCrawlerDaemon.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private CrawlerDao crawlerDao;

    private final Map<String, ActivityCrawler> crawlers = new LinkedHashMap<String, ActivityCrawler>();

    public void registerCrawler(ActivityCrawler crawler) {
        assert crawler.getName() != null;

        LOG.info("registering crawler : name = '{}', crawler = '{}'", crawler.getName(), crawler);
        crawlers.put(crawler.getName(), crawler);
    }

    public void crawl() {
        List<User> users = userDao.getAllUsers();
        LOG.info("crawling for : " + users.size() + " user(s)");
        for (User u : users) {
            LOG.debug("crawling for user = " + u.getId() + " with " + crawlers.size() + " crawler(s)");
            for (Map.Entry<String, ActivityCrawler> e : crawlers.entrySet()) {
                String crawlerName = e.getKey();
                ActivityCrawler crawler = e.getValue();

                LOG.debug("[{} : {}] crawling for user", u.getId(), crawlerName);

                long lastCrawled = 0;

                CrawlInfo info = crawlerDao.getCrawlInfo(u, crawlerName);
                if (info == null) {
                    info = new CrawlInfo();
                    info.setUser(u);
                    info.setName(crawlerName);
                }

                lastCrawled = (info.getTimestamp() == null ? System.currentTimeMillis() - 2 * 60 * 60 * 1000 : info.getTimestamp());

                List<Activity> activities = crawler.crawl(u, lastCrawled);
                if (activities == null || activities.isEmpty()) {
                    LOG.debug("[{} : {}] crawler got no activities", u.getId(), crawlerName);
                    continue;
                }
                LOG.info("[{} : {}] crawler got " + activities.size() + " activities", u.getId(), crawlerName);

                long now = System.currentTimeMillis();

                info.setTimestamp(now);
                crawlerDao.merge(info);


                for (Activity a : activities) {
                    a.setUser(u);
                    a.setCategorized(false);
                    if (a.getTimeStamp() == null) {
                        a.setTimeStamp(now);
                    }
                    LOG.info("[{} : {}] adding activity : " + a, u.getId(), crawlerName);
                    activityDao.merge(a);
                }
            }
        }
    }

}
