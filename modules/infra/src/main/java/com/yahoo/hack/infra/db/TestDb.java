package com.yahoo.hack.infra.db;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.ActivityDao;
import com.yahoo.hack.infra.model.CheckinActivity;
import com.yahoo.hack.infra.model.Device;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.infra.model.SuggestionDao;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.infra.model.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * @since 10/1/11
 */
public class TestDb {

    public static void main(String... args) throws Exception {
        String[] confs = {"infra-spring.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(confs);
        UserDao userDao = ctx.getBean(UserDao.class);
        ActivityDao activityDao = ctx.getBean(ActivityDao.class);
        SuggestionDao suggestionDao = ctx.getBean(SuggestionDao.class);

        User user = new User();
        user = userDao.merge(user);
        System.out.println(user);

        Suggestion s = new Suggestion();
        s.setUser(user);
        s.setTimeStamp(System.currentTimeMillis());



        Activity a = new CheckinActivity();
        a.setTimeStamp(new Date().getTime());
        a.setUser(user);
        a.setDevice(Device.MOBILE);
        //a.addCategory(cat);

        a = activityDao.merge(a);

//        List<Activity> list = activityDao.getActivities(user);

    }

}
