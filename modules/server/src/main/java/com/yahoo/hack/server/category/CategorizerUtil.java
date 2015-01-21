package com.yahoo.hack.server.category;

import com.yahoo.hack.infra.model.FacebookActivity;
import com.yahoo.hack.server.util.BossClient;
import com.yahoo.hack.server.util.BossResult;
import com.yahoo.hack.server.util.HttpClient;
import com.yahoo.hack.server.util.TermExtractor;
import com.yahoo.hack.server.util.facebook.HomeFeedEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: vikashk
 * Date: 10/11/11
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategorizerUtil {
    private StringBuilder query;
    private TermExtractor termExtractor;
    private BossClient bossClient;

    private BossResult[] bossResults;
    private final int NUM_OF_SITES = 6;
    private HashMap<String, String> keyWordsDescription;
    private List<String> terms = new ArrayList<String>();
    private WebSiteReader crawler;


    public CategorizerUtil() {
        query = new StringBuilder();
        termExtractor = new TermExtractor();
        bossClient = new BossClient();
        bossResults = new BossResult[NUM_OF_SITES];
    }

    private String getMessageTerms(FacebookActivity entry) {
        String message = "";

        if (entry.getMessage() != null) {
            message += entry.getMessage();
        }
         if (entry.getDescription() != null) {
             message += " " + entry.getDescription();
         }

        if (entry.getName() != null) {
            message += " " + entry.getName();
        }
        List<String> messageTerm = termExtractor.extract(message);
        int termCount = 0;
        String messagequery = "";
        for (String s : messageTerm) {
            messagequery = messagequery + s + " ";
            termCount++;
            if (termCount == 2) break;
            terms.add(s);
        }
        return messagequery;
    }

     private void getKeyWordsDescription(String url) throws Exception {
        crawler = new WebSiteReader(url);
        keyWordsDescription = crawler.getMetaKeywords_Description();

    }

    private String getKeyWords() {
        if(keyWordsDescription.containsKey("keywords"))
            return keyWordsDescription.get("keywords");
        return "";
    }

    private String getDescription() {
        if(keyWordsDescription.containsKey("Description"))
            return keyWordsDescription.get("Description");
        return "";
    }

    public CategorizerResults getCategory(FacebookActivity entry) throws Exception {
        if (entry.getLink() != null) {
         //   HttpClient httpClient = new HttpClient();
            String link = entry.getLink();


            getKeyWordsDescription(link);
            String keyword = getKeyWords();
            List<String> termsFromKeyword = termExtractor.extract(keyword);
            int keyWordCount=0;
            if(termsFromKeyword!=null){
                for(String s : termsFromKeyword){
                    if(keyWordCount<2){
                        query.append(s+" ");
                        keyWordCount++;
                        terms.add(s);
                    }
                    else break;

                }
            }
         /*   if (keyword != null){
                query.append(keyword + " ");
                terms.add(keyword);
            }
         */

            /* description from html source itsef
                Can be changed to getDescription from HomeFeedentry
                String description= entry.getName()+" "+ getDescription();
             */
            String description = getDescription();

            List<String> termsFromExtractor = termExtractor.extract(description);
          /*  if(termsFromExtractor!=null){
                for (String s : termsFromExtractor) {
                    terms.add(s);
                }
            }
          */
            int termCount = 0;
            if(termsFromExtractor!=null){
                for (String s : termsFromExtractor) {
                    if(termCount<2){
                        query.append(s + " ");
                        termCount++;
                        terms.add(s);
                    }
                    else break;

                }
            }

            query = new StringBuilder(query.toString().trim());
            if (query.toString().length() < 2) {
                String s = getMessageTerms(entry);
                query.append(s);

            }
        } else {
            String messageQuery = getMessageTerms(entry);
            query.append(messageQuery);
        }
        String queryToString = query.toString();

        bossResults[0] = bossClient.call(queryToString, "movies.yahoo.com");
        bossResults[1] = bossClient.call(queryToString, "cricket.yahoo.com");
        bossResults[2] = bossClient.call(queryToString, "news.yahoo.com");
        bossResults[3] = bossClient.call(queryToString, "finance.yahoo.com");
        bossResults[4] = bossClient.call(queryToString, "omg.yahoo.com");
        bossResults[5] = bossClient.call(queryToString, "answers.yahoo.com");

        //sort BossResults[] on basis of TotalResults
        for (int i = 0; i < NUM_OF_SITES - 1; i++) {
            for (int j = i + 1; j < NUM_OF_SITES; j++) {
                if (bossResults[i] != null && bossResults[j] != null) {
                    if (bossResults[i].getTotalResults() < bossResults[j].getTotalResults()) {
                        BossResult temp = bossResults[i];
                        bossResults[i] = bossResults[j];
                        bossResults[j] = temp;
                    }
                }
            }
        }

        List<String> categoryResults = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            if (bossResults[i] == null || bossResults[i].getTotalResults() == 0) {
                continue;
            }
            List<String> l = bossResults[i].getSites();
            String site = l.get(0);
            int find = site.indexOf(".yahoo.com");
            String siteName = site.substring(0, find);
            if (siteName.equals("omg")) {
                siteName = "movies";
            }
            categoryResults.add((siteName));
        }


        CategorizerResults results = new CategorizerResults();
        results.setQueryterms(queryToString);
        results.setCategories(categoryResults);
        results.setTerms(terms);

        return results;
    }
}
