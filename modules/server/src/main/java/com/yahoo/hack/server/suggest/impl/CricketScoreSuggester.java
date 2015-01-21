package com.yahoo.hack.server.suggest.impl;

import com.yahoo.hack.infra.model.CricketScoreSuggestion;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.server.suggest.Suggester;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @since 10/11/11
 */
public class CricketScoreSuggester implements Suggester {

    public String getCategory() {
        return "cricket";
    }

    public List<Suggestion> suggest(Set<String> categories, Set<String> terms, Set<String> queries) {
        CricketScoreSuggestion suggestion = new CricketScoreSuggestion();
        suggestion.setName("Champions League Twenty20, 2011");
        suggestion.setScore("RCB: 108/10 (19.2), RR: 5.58");
        suggestion.setVenue("MA Chidambaram Stadium, Chennai");
        return Arrays.asList(new Suggestion[]{suggestion});
    }
}
