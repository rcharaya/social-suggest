package com.yahoo.hack.server.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yahoo.hack.infra.config.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @since 10/11/11
 */
public class BossClient {

    private static final Logger LOG = LoggerFactory.getLogger(BossClient.class);

    public BossResult call(String q, String... sites) {
        q = (q == null ? "" : q.trim());
        BossResult bossResult = new BossResult();
        if (sites != null && sites.length > 0) {
            bossResult.setSites(Arrays.asList(sites));
        }

        if (q.length() == 0 || "null".equals(q)) {
            return bossResult;
        }
        YqlClient client = new YqlClient();
        client.setStore("store://datatables.org/alltableswithkeys");

        String query = "select * from boss.search where q=\"" + q + "\" and ck=\"" + Credentials.getProperty(Credentials.BOSS_APP_ID) + "\" and secret=\"" + Credentials.getProperty(Credentials.BOSS_APP_SECRET) + "\"";
        if (sites != null && sites.length > 0) {
            query += " and sites = \"" + sites[0] + "\";";
        }
        try {
            String data = client.call(query);
            JsonParser parser = new JsonParser();
            JsonObject root = parser.parse(data).getAsJsonObject().get("query").getAsJsonObject();
            JsonObject webResults = root.get("results").getAsJsonObject();
            if (webResults == null || webResults.isJsonNull()) {
                return bossResult;
            }

            webResults = webResults.getAsJsonObject().get("bossresponse").getAsJsonObject().get("web").getAsJsonObject();

            if (webResults.has("count")) {
                bossResult.setCount(webResults.get("count").getAsInt());
            }
            if (webResults.has("totalresults")) {
                bossResult.setTotalResults(webResults.get("totalresults").getAsLong());
            }
            if (sites != null && sites.length > 0) {
                bossResult.setSites(Arrays.asList(sites));
            }

            JsonElement el = webResults.get("results");
            if (el == null || el.isJsonNull()) {
                return bossResult;
            }

            el = el.getAsJsonObject().get("result");
            if (el == null || el.isJsonNull()) {
                return bossResult;
            }

            try {
                if (el.isJsonObject()) {
                    JsonObject link = el.getAsJsonObject();
                    BossLink bossLink = new BossLink();
                    if (link.has("clickurl")) {
                        bossLink.setClickurl(link.get("clickurl").getAsString());
                    }
                    if (link.has("url")) {
                        bossLink.setUrl(link.get("url").getAsString());
                    }
                    if (link.has("dispurl")) {
                        bossLink.setDispurl(link.get("dispurl").getAsJsonObject().get("content").getAsString());
                    }
                    bossLink.setTitle(link.get("title").getAsJsonObject().get("content").getAsString());
                    bossLink.setSnippet(link.get("abstract").getAsJsonObject().get("content").getAsString());
                    bossResult.addLink(bossLink);
                } else {

                    JsonArray links = el.getAsJsonArray();

                    for (int i = 0; i < links.size(); i++) {
                        JsonObject link = links.get(i).getAsJsonObject();

                        BossLink bossLink = new BossLink();
                        bossLink.setClickurl(link.get("clickurl").getAsString());
                        bossLink.setUrl(link.get("url").getAsString());
                        bossLink.setDispurl(link.get("dispurl").getAsJsonObject().get("content").getAsString());
                        bossLink.setTitle(link.get("title").getAsJsonObject().get("content").getAsString());
                        bossLink.setSnippet(link.get("abstract").getAsJsonObject().get("content").getAsString());
                        bossResult.addLink(bossLink);

                    }
                }
            } catch (Exception e) {
                LOG.error("error occured while parsing link : ", e);
            }

            return bossResult;
        } catch (Exception e) {
            LOG.error("error occured while fetching result", e);

        }
        return bossResult;
    }

}
