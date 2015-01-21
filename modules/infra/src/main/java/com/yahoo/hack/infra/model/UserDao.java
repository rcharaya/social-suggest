package com.yahoo.hack.infra.model;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @since 10/1/11
 */
@Repository(value = "userDao")
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> getAllUsers() {
        Query q = entityManager.createQuery("from " + User.class.getName() + " u");

        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            List<User> users = new ArrayList<User>(list.size());
            for (Object o : list) {
                users.add((User)o);
            }
            return users;
        }
        return Collections.EMPTY_LIST;
    }

    public User merge(User user) {
        assert user.getOpenid() != null;

        User old = getUserByOpenId(user.getOpenid());
        if (old != null) {
            user.setId(old.getId());
        }

        user = entityManager.merge(user);
        return user;
    }

    public User getUser(Long id) {
        return entityManager.find(User.class, id);
    }

    public User getUserByOpenId(String openid) {
        Query q = entityManager.createQuery("from " + User.class.getName() + " u where u.openid = :openid");
        q.setParameter("openid", openid);
        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            return (User)list.get(0);
        }
        return null;
    }
}
