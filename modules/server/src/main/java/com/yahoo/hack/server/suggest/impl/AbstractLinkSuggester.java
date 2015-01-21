package com.yahoo.hack.server.suggest.impl;

import com.yahoo.hack.infra.model.LinkSuggestion;
import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.server.suggest.Suggester;
import com.yahoo.hack.server.util.BossClient;
import com.yahoo.hack.server.util.BossLink;
import com.yahoo.hack.server.util.BossResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @since 10/11/11
 */
public abstract class AbstractLinkSuggester implements Suggester {

    private static final long MAX_LINK_SUGGEST_COUNT = 1;

    private final String category;
    private final String site;

    public AbstractLinkSuggester(String category, String site) {
        this.category = category;
        this.site = site;
    }

    public String getCategory() {
        return category;
    }

    public List<Suggestion> suggest(Set<String> categories, Set<String> terms, Set<String> queries) {
        StringBuilder str = new StringBuilder();
        for (String s : terms) {
            str.append("'").append(s).append("'").append(" ");
        }
        BossClient client = new BossClient();
        BossResult result = client.call(str.toString(), site);
        List<BossLink> links = result.getLinks();

        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        if (links != null && !links.isEmpty()) {
            for (int i = 0; i < MAX_LINK_SUGGEST_COUNT && i < links.size(); i++) {
                BossLink bossLink = links.get(i);
                LinkSuggestion suggestion = new LinkSuggestion();
                suggestion.setSnippet(bossLink.getSnippet());
                suggestion.setTitle(bossLink.getTitle());
                suggestion.setUrl(bossLink.getUrl());
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }
}
