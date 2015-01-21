package com.yahoo.hack.server.activity;

import com.yahoo.hack.server.activity.impl.DummyCheckinActivityCrawler;
import com.yahoo.hack.server.activity.impl.DummyYQLCrawler;
import com.yahoo.hack.server.activity.impl.FacebookStreamCrawler;
import com.yahoo.hack.server.activity.impl.TwitterStatusCrawler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @since 10/1/11
 */
public class ActivityCrawlerMain {

   public static void main(String... args) throws Exception {
        String[] confs = {"infra-spring.xml", "server-spring.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(confs);
        final ActivityCrawlerDaemon daemon = ctx.getBean(ActivityCrawlerDaemon.class);

        registerCrawlers(daemon);

        new Timer("activity-timer").schedule(new TimerTask() {
            @Override
            public void run() {
                daemon.crawl();
            }
        }, 0, 2000);
    }


    private static void registerCrawlers(ActivityCrawlerDaemon daemon) {
//        daemon.registerCrawler(new DummyCheckinActivityCrawler());
//        daemon.registerCrawler(new DummyYQLCrawler());
        daemon.registerCrawler(new FacebookStreamCrawler());
        daemon.registerCrawler(new TwitterStatusCrawler());
    }

}
