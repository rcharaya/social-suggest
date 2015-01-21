package com.yahoo.hack.server.suggest;

import com.yahoo.hack.infra.model.Suggestion;
import com.yahoo.hack.server.suggest.impl.AnswersSuggester;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 10/16/11
 */
public class AnswesSuggesterTest {

    @Test
    public void testSuggest() {
        AnswersSuggester suggester = new AnswersSuggester();
        Set<String> categories = new HashSet<String>();
        Set<String> terms = new HashSet<String>();
        Set<String> queries = new HashSet<String>();
        terms.add("nokia dual sim");
        List<Suggestion> suggestions = suggester.suggest(categories, terms, queries);
    }

}
