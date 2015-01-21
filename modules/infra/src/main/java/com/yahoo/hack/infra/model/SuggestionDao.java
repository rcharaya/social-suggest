package com.yahoo.hack.infra.model;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 10/1/11
 */
@Repository
public class SuggestionDao {

    @PersistenceContext
    EntityManager entityManager;

    public Suggestion merge(Suggestion suggestion) {
        assert suggestion.getUser() != null;
        assert suggestion.getTimeStamp() != null;

        suggestion = entityManager.merge(suggestion);
        return suggestion;
    }

    public List<Suggestion> getSuggestions(User user) {
        Query q = entityManager.createQuery("from " + Suggestion.class.getName() + " a where a.user = :user order by a.timeStamp desc");
        q.setParameter("user", user);
        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            List<Suggestion> activities = new ArrayList<Suggestion>();
            for (Object o : list) {
                activities.add((Suggestion) o);
            }
            return activities;
        }

        return Collections.EMPTY_LIST;
    }


}
