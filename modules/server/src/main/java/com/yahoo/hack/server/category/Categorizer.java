package com.yahoo.hack.server.category;

import com.yahoo.hack.infra.model.Activity;

import java.util.Set;

/**
 * @since 10/10/11
 */
public interface Categorizer {

    String getName();

    boolean categorize(Activity activity, String text, Set<String> terms);

}
