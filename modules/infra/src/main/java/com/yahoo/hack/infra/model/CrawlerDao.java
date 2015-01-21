package com.yahoo.hack.infra.model;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @since 10/1/11
 */
@Repository
public class CrawlerDao {

    @PersistenceContext
    EntityManager entityManager;

    public CrawlInfo merge(CrawlInfo c) {
        assert c.getUser() != null;
        assert c.getName() != null;

        CrawlInfo old = getCrawlInfo(c.getUser(), c.getName());
        if (old != null) {
            c.setId(old.getId());
        }

        c = entityManager.merge(c);
        return c;
    }

    public CrawlInfo getCrawlInfo(User user, String name) {
        Query q = entityManager.createQuery("from " + CrawlInfo.class.getName() + " a where a.user = :user and a.name = :name");
        q.setParameter("user", user);
        q.setParameter("name", name);
        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            return (CrawlInfo)list.get(0);
        }
        return null;
    }

}
