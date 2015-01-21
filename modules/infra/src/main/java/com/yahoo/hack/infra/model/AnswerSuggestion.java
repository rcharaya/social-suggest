package com.yahoo.hack.infra.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @since 10/16/11
 */
@Entity
public class AnswerSuggestion extends Suggestion {

    @Column(length = 1024)
    private String subject;

    @Column(length = 1024)
    private String content;

    @Column(length = 1024)
    private String solution;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
