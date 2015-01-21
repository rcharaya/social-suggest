package com.yahoo.hack.server.suggest;

import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.server.suggest.impl.MoviesLinkSuggester;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 10/11/11
 */
public class LinkSuggesterTest {

    private Suggester suggester;

    @BeforeTest
    public void setupTest() {
        suggester = new MoviesLinkSuggester();
    }

    @Test
    public void testSuggestion() {
        Set<String> categories = new HashSet<String>();
        Set<String> terms = new HashSet<String>();
        Set<String> queries = new HashSet<String>();

        terms.add("steve jobs");

        List<Suggestion> suggestions = suggester.suggest(categories, terms, queries);

    }

}
