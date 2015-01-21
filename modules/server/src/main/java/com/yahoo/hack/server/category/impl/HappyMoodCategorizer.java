package com.yahoo.hack.server.category.impl;

import com.yahoo.hack.infra.model.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @since 10/10/11
 */
public class HappyMoodCategorizer extends AbstractLocalCategorizer {

    private static final Logger LOG = LoggerFactory.getLogger(HappyMoodCategorizer.class);

    public HappyMoodCategorizer() {
        super("happy.txt");
    }

    public String getName() {
        return "happy-mood";
    }

    public boolean categorize(Activity activity, String text, Set<String> terms) {
        double score = computScore(text);

        if (score <= 0) {
            return false;
        }
        activity.addCategory("happy");
        return true;
    }
}
