package com.yahoo.hack.infra.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @since 10/11/11
 */
@Entity
public class LinkSuggestion extends Suggestion {

    @Column(length = 1024)
    private String title;

    @Column(length = 1024)
    private String url;

    @Column(length = 1024)
    private String snippet;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }
}
