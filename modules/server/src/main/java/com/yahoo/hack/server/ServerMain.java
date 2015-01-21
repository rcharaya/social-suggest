package com.yahoo.hack.server;

import com.yahoo.hack.server.activity.ActivityCrawlerMain;
import com.yahoo.hack.server.category.CategorizerMain;
import com.yahoo.hack.server.delivery.DeliveryMain;
import com.yahoo.hack.server.suggest.SuggestMain;

/**
 * @since 10/10/11
 */
public class ServerMain {

    public static void main(String ...args) throws Exception {
        ActivityCrawlerMain.main();
        CategorizerMain.main();
        SuggestMain.main();
        DeliveryMain.main();
    }

}
