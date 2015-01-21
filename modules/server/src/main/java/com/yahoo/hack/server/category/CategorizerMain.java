package com.yahoo.hack.server.category;

import com.yahoo.hack.server.category.impl.HappyMoodCategorizer;
import com.yahoo.hack.server.category.impl.SadMoodCategorizer;
import com.yahoo.hack.server.category.impl.SmartCategorizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @since 10/10/11
 */
public class CategorizerMain {

    public static void main(String... args) throws Exception {
        String[] confs = {"server-spring.xml", "infra-spring.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(confs);
        final CategorizerDaemon daemon = ctx.getBean(CategorizerDaemon.class);
        registerCategorizers(daemon);

        new Timer("categorizer-timer").schedule(new TimerTask() {
            @Override
            public void run() {
                daemon.categorize();
            }
        }, 0, 2000);
    }

    private static void registerCategorizers(CategorizerDaemon daemon) {
        daemon.registerCrawler(new HappyMoodCategorizer());
        daemon.registerCrawler(new SadMoodCategorizer());
        daemon.registerCrawler(new SmartCategorizer());
    }

}
