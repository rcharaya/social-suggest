package com.yahoo.hack.server.category;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.ActivityDao;
import com.yahoo.hack.server.util.ActivityTextExtractor;
import com.yahoo.hack.server.util.TermExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @since 10/10/11
 */
public class CategorizerDaemon {

    private static final Logger LOG = LoggerFactory.getLogger(CategorizerDaemon.class);

    private final ActivityTextExtractor textExtractor = new ActivityTextExtractor();
    private final TermExtractor termExtractor = new TermExtractor();

    private final Map<String, Categorizer> categorizers = new LinkedHashMap<String, Categorizer>();

    public void registerCrawler(Categorizer categorizer) {
        assert categorizer.getName() != null;

        LOG.info("registering categorizer : name = '{}', categorizer = '{}'", categorizer.getName(), categorizer);
        categorizers.put(categorizer.getName(), categorizer);
    }


    @Autowired
    ActivityDao activityDao;

    public void categorize() {
        List<Activity> activities = activityDao.getUnCategorizedActivities();
        LOG.info("uncategorized activities : {}", activities.size());
        if (activities.isEmpty()) {
            return;
        }

        for (Activity activity : activities) {
            if (activity.isCategorized()) {
                continue;
            }

            activity.setCategorized(true);

            String text = textExtractor.extract(activity);
            Set<String> terms = new HashSet<String>();
            terms.addAll(termExtractor.extract(text));

            if (terms != null && !terms.isEmpty()) {
                activity.setTerms(terms);
            }

            for (Map.Entry<String, Categorizer> e : categorizers.entrySet()) {
                String catName = e.getKey();
                Categorizer cat = e.getValue();

                cat.categorize(activity, text, terms);
            }

            activity = activityDao.merge(activity);
            LOG.debug("categorized activitiy : {}, categories : {}, terms = " + activity.getTerms(), activity, activity.getCategories());
        }
    }
}
