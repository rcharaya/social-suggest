package com.yahoo.hack.site;

import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.infra.model.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @since 10/4/11
 */
public class SitePersistence {

    private UserDao userDao;

    public SitePersistence() throws Exception {
        String[] confs = {"infra-spring.xml"};
        ApplicationContext ctx = new ClassPathXmlApplicationContext(confs);
        this.userDao = ctx.getBean(UserDao.class);
    }

    public User storeUser(String openid) {
        User user = userDao.getUserByOpenId(openid);
        if (user == null) {
            user = new User();
        }
        user.setOpenid(openid);
        return userDao.merge(user);
    }

    public User getUser(Long id) {
        return userDao.getUser(id);
    }

    public User mergeUser(User user) {
        return userDao.merge(user);
    }

}
