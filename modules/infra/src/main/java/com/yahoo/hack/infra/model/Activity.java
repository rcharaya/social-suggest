package com.yahoo.hack.infra.model;

import org.hibernate.annotations.Fetch;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 10/1/11
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column
    private Long timeStamp;

    @Column(length = 1024)
    private String queryString;

    @Column
    @Enumerated(EnumType.STRING)
    private Device device;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> categories = new HashSet<String>();

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> terms = new HashSet<String>();

    @Column
    private boolean categorized;

    @Column
    private boolean suggested;

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public boolean isCategorized() {
        return categorized;
    }

    public void setCategorized(boolean categorized) {
        this.categorized = categorized;
    }

    public boolean isSuggested() {
        return suggested;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }

    public void addTerm(String term) {
        term = (term == null ? "" : term.trim().toLowerCase());
        if (term.length() == 0) {
            return;
        }

        terms.add(term);
    }

    public Set<String> getTerms() {
        return terms;
    }

    public void setTerms(Set<String> terms) {
        this.terms = terms;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void addCategory(String category) {
        category = (category == null ? "" : category.trim().toLowerCase());
        if (category.length() == 0) {
            return;
        }

        this.categories.add(category);
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
