package com.yahoo.hack.server.category.impl;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.FacebookActivity;
import com.yahoo.hack.infra.model.TweetActivity;
import com.yahoo.hack.server.category.Categorizer;
import com.yahoo.hack.server.category.CategorizerResults;
import com.yahoo.hack.server.category.CategorizerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * @since 10/11/11
 */
public class SmartCategorizer implements Categorizer {

    private static final Logger LOG = LoggerFactory.getLogger(SmartCategorizer.class);

    public String getName() {
        return "smart";
    }

    public boolean categorize(Activity activity, String text, Set<String> terms) {
        try {
            CategorizerResults results = null;
            if (activity instanceof TweetActivity) {
                FacebookActivity fbActivity = new FacebookActivity();
                fbActivity.setMessage(((TweetActivity)activity).getStatus());
                results = new CategorizerUtil().getCategory(fbActivity);
            } else if (activity instanceof FacebookActivity) {
                results = new CategorizerUtil().getCategory((FacebookActivity) activity);
            }

            if (results != null) {
                Set<String> categories = new HashSet<String>();
                categories.addAll(results.getCategories());
                activity.setCategories(categories);

                Set<String> termsSet = new HashSet<String>();
                if (results.getTerms() != null) {
                    termsSet.addAll(results.getTerms());
                }
                activity.setTerms(termsSet);
                return true;
            }
        } catch (Exception e) {
            LOG.error("error ocured while categorizing activity", e);
        }

        return false;
    }
}
