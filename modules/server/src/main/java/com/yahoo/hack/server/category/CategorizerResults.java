package com.yahoo.hack.server.category;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vikashk
 * Date: 10/11/11
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategorizerResults {
    private String queryterms;
    private List<String> categories;
    private List<String> terms;

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public String getQueryterms() {
        return queryterms;
    }

    public void setQueryterms(String queryterms) {
        this.queryterms = queryterms;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
