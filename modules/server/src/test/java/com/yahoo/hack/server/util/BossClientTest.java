package com.yahoo.hack.server.util;

import com.google.gson.Gson;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @since 10/11/11
 */
public class BossClientTest {

    private BossClient client;

    @BeforeTest
    public void setupTest() {
        client = new BossClient();
    }

    @Test
    public void testCall() {
        BossResult result = client.call("amitabh", "cricket.yahoo.com", "movies.yahoo.com");
        String str = new Gson().toJson(result);
    }
}
