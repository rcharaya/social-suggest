package com.yahoo.hack.server.suggest;

import com.yahoo.hack.infra.model.Suggestion;

import java.util.List;
import java.util.Set;

/**
 * @since 10/11/11
 */
public interface Suggester {

    String getCategory();

    List<Suggestion> suggest(Set<String> categories, Set<String> terms, Set<String> queries);

}
