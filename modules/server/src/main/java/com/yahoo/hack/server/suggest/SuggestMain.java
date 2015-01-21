package com.yahoo.hack.server.suggest;

import com.yahoo.hack.server.suggest.impl.AnswersSuggester;
import com.yahoo.hack.server.suggest.impl.CricketLinkSuggester;
import com.yahoo.hack.server.suggest.impl.CricketScoreSuggester;
import com.yahoo.hack.server.suggest.impl.FinanceLinkSuggester;
import com.yahoo.hack.server.suggest.impl.MoviesLinkSuggester;
import com.yahoo.hack.server.suggest.impl.NewsLinkSuggester;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @since 10/10/11
 */
public class SuggestMain {

    public static void main(String... args) throws Exception {
        String[] confs = {"server-spring.xml", "infra-spring.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(confs);
        final SuggestionDaemon daemon = ctx.getBean(SuggestionDaemon.class);
        registerSuggesters(daemon);

        new Timer("suggest-timer").schedule(new TimerTask() {
            @Override
            public void run() {
                daemon.run();
            }
        }, 0, 2000);
    }

    private static void registerSuggesters(SuggestionDaemon daemon) {
        daemon.registerSuggester(new CricketLinkSuggester());
        daemon.registerSuggester(new MoviesLinkSuggester());
        daemon.registerSuggester(new NewsLinkSuggester());
        daemon.registerSuggester(new CricketScoreSuggester());
        daemon.registerSuggester(new FinanceLinkSuggester());
        daemon.registerSuggester(new AnswersSuggester());
    }

}
