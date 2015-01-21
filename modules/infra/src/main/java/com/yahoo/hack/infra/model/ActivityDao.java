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
public class ActivityDao {

    @PersistenceContext
    EntityManager entityManager;

    public Activity merge(Activity activity) {
        assert activity.getUser() != null;
        assert activity.getTimeStamp() != null;

        activity = entityManager.merge(activity);
        return activity;
    }

    public List<Activity> getUnCategorizedActivities() {
        return getActivitiesByCategory(false);
    }

    public List<Activity> getCategorizedActivities(User user) {
        Query q = entityManager.createQuery("from " + Activity.class.getName() + " a where a.user = :user and a.categorized = :categorized and a.suggested = :suggested order by a.timeStamp desc");
        q.setParameter("categorized", true);
        q.setParameter("suggested", false);
        q.setParameter("user", user);
        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            List<Activity> activities = new ArrayList<Activity>();
            for (Object o : list) {
                activities.add((Activity) o);
            }
            return activities;
        }

        return Collections.EMPTY_LIST;
    }

    private List<Activity> getActivitiesByCategory(boolean categorized) {
        Query q = entityManager.createQuery("from " + Activity.class.getName() + " a where a.categorized = :categorized order by a.timeStamp desc");
        q.setParameter("categorized", categorized);
        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            List<Activity> activities = new ArrayList<Activity>();
            for (Object o : list) {
                activities.add((Activity) o);
            }
            return activities;
        }

        return Collections.EMPTY_LIST;
    }

    public List<Activity> getActivities(User user) {
        Query q = entityManager.createQuery("from " + Activity.class.getName() + " a where a.user = :user and a.processed = :processed order by a.timeStamp desc");
        q.setParameter("user", user);
        q.setParameter("processed", false);
        List<Object> list = q.getResultList();
        if (list != null && !list.isEmpty()) {
            List<Activity> activities = new ArrayList<Activity>();
            for (Object o : list) {
                activities.add((Activity) o);
            }
        }

        return Collections.EMPTY_LIST;
    }

}
