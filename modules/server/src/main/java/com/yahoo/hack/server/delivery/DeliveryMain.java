package com.yahoo.hack.server.delivery;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.yahoo.hack.infra.model.LinkSuggestion;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.infra.model.SuggestionDao;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.infra.model.UserDao;
import com.yahoo.hack.server.suggest.SuggestionDaemon;
import com.yahoo.hack.server.util.FacebookClient;
import com.yahoo.hack.server.util.ParamsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @since 10/12/11
 */
public class DeliveryMain {
    public static void main(String... args) throws Exception {
        String[] confs = {"server-spring.xml", "infra-spring.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(confs);
        final DeliveryDaemon daemon = ctx.getBean(DeliveryDaemon.class);
//        registerSuggesters(daemon);

        new Timer("suggest-timer").schedule(new TimerTask() {
            @Override
            public void run() {
                daemon.run();
            }
        }, 0, 2000);
    }
}
