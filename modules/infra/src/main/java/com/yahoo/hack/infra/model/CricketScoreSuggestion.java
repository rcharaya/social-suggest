package com.yahoo.hack.infra.model;

import javax.persistence.Entity;

/**
 * @since 10/11/11
 */
@Entity
public class CricketScoreSuggestion extends Suggestion {

    private String name;
    private String venue;
    private String score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
