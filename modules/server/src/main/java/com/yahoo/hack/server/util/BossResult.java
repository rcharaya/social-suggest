package com.yahoo.hack.server.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 10/11/11
 */
public class BossResult {

    private int count;
    private long totalResults;
    private List<BossLink> links = new ArrayList<BossLink>();
    private List<String> sites = new ArrayList<String>();

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> siteSearched) {
        this.sites = siteSearched;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public List<BossLink> getLinks() {
        return links;
    }

    public void addLink(BossLink link) {
        if (link == null) {
            return;
        }
        links.add(link);
    }

    public void setLinks(List<BossLink> links) {
        this.links = links;
    }
}
