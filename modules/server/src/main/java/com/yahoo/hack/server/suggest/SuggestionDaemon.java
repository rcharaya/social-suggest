package com.yahoo.hack.server.suggest;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.ActivityDao;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.infra.model.SuggestionDao;
import com.yahoo.hack.infra.model.User;
import com.yahoo.hack.infra.model.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @since 10/11/11
 */
public class SuggestionDaemon {

    private static final Logger LOG = LoggerFactory.getLogger(SuggestionDaemon.class);

    private static final long TIME_PERIOD = 2 * 60 * 60 * 1000;
    private static final int MAX_TOP_CATEGORIES = 2;
    private static final double NORMALIZATION_FACTOR = 1000.0;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SuggestionDao suggestionDao;

    private List<Suggester> suggesters = new ArrayList<Suggester>();


    public void run() {
        List<User> users = userDao.getAllUsers();
        LOG.info("running suggester for {} user(s)", users.size());
        for (User user : users) {
            List<Activity> activities = activityDao.getCategorizedActivities(user);
            long now = System.currentTimeMillis();

            LOG.info("running suggester for user = {}, activities = {}", user, activities.size());

            if (activities.isEmpty()) {
                continue;
            }

            Map<String, Double> categoryMap = new HashMap<String, Double>();

            computeCategoryScore(activities, now, categoryMap);
            LOG.debug("computed category score for user = {}, scoreMap = {}", user, categoryMap);

            List<CategoryStat> list = sortCategories(categoryMap);

            Set<String> topCategories = getTopCategories(list);
            LOG.debug("top categories for user = {}, topCategories = {}", user, topCategories);

            Set<String> queryStrings = new HashSet<String>();
            Set<String> terms = new HashSet<String>();

            for (Activity a : activities) {
                if (a.isSuggested() || a.getTimeStamp() == null || a.getTimeStamp() < (now - TIME_PERIOD)) {
                    a.setSuggested(true);
                    a = activityDao.merge(a);
                    continue;
                }

                a.setSuggested(true);
                a = activityDao.merge(a);

                Set<String> categories = a.getCategories();
                if (categories == null && !categories.isEmpty()) {
                    continue;
                }

                for (String cat : categories) {
                    if (!topCategories.contains(cat)) {
                        continue;
                    }

                    terms.addAll(a.getTerms());
                    String q = "";
                    for (String t : a.getTerms()) {
                        q += t + " ";
                    }
                    q = (q == null ? "" : q.trim().toLowerCase());
                    if (q.length() > 0) {
                        queryStrings.add(q);
                    }
                }

                for (Suggester s : suggesters) {
                    if (!topCategories.contains(s.getCategory())) {
                        continue;
                    }

                    List<Suggestion> suggestions = s.suggest(topCategories, terms, queryStrings);
                    if (suggestions == null || suggestions.isEmpty()) {
                        continue;
                    }
                    LOG.debug("got suggestions for user = {}, suggestions = {}", user, suggestions.size());
                    for (Suggestion t : suggestions) {
                        t.setUser(user);
                        t.setTimeStamp(now);
                        t = suggestionDao.merge(t);
                    }
                }
            }
        }
    }

    private void computeCategoryScore(List<Activity> activities, long now, Map<String, Double> categoryMap) {
        for (Activity a : activities) {
            if (a.getTimeStamp() == null || a.getTimeStamp() < (now - TIME_PERIOD)) {
                continue;
            }

            Set<String> categories = a.getCategories();
            if (categories != null && !categories.isEmpty()) {
                for (String cat : categories) {
                    Double total = categoryMap.get(cat);
                    if (total == null) {
                        total = 0.0;
                    }
                    double score = computeScore(1, now - a.getTimeStamp());
                    total += score;
                    categoryMap.put(cat, total);
                }
            }
        }
    }

    private Set<String> getTopCategories(List<CategoryStat> list) {
        Set<String> topCategories = new LinkedHashSet<String>();
        for (int i = 0; i < MAX_TOP_CATEGORIES && i < list.size(); i++) {
            topCategories.add(list.get(i).category);
        }
        return topCategories;
    }

    private List<CategoryStat> sortCategories(Map<String, Double> categoryMap) {
        List<CategoryStat> list = new ArrayList<CategoryStat>();
        for (Map.Entry<String, Double> e : categoryMap.entrySet()) {
            CategoryStat stat = new CategoryStat();
            stat.category = e.getKey();
            stat.score = e.getValue();
            list.add(stat);
        }

        Collections.sort(list, new Comparator<CategoryStat>() {
            public int compare(CategoryStat x, CategoryStat y) {
                if (x.score > y.score) {
                    return -1;
                }
                if (x.score < y.score) {
                    return 1;
                }
                return 0;
            }
        });
        return list;
    }


    private class CategoryStat {
        private String category;
        private double score;
    }

    private double computeScore(int count, long delay) {
        return ((double) count) * Math.exp(-delay / NORMALIZATION_FACTOR);
    }

    public void registerSuggester(Suggester suggester) {
        this.suggesters.add(suggester);
    }


}
