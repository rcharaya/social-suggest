package com.yahoo.hack.server.category.impl;

import com.yahoo.hack.infra.model.Activity;

import java.util.Set;

/**
 * @since 10/10/11
 */
public class SadMoodCategorizer extends AbstractLocalCategorizer {

    public SadMoodCategorizer() {
        super("sad.txt");
    }

    public String getName() {
        return "sad-mood";
    }

    public boolean categorize(Activity activity, String text, Set<String> terms) {
        double score = computScore(text);

        if (score <= 0) {
            return false;
        }
        activity.addCategory("sad");
        return true;
    }
}
