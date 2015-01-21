package com.yahoo.hack.infra.config;

import java.io.IOException;
import java.util.Properties;

/**
 * @since 10/4/11
 */
public class Credentials {

    public static final String FACEBOOK_APP_ID = "facebook.app_id";
    public static final String FACEBOOK_APP_SECRET = "facebook.app_secret";

    public static final String TWITTER_APP_ID = "twitter.app_id";
    public static final String TWITTER_APP_SECRET = "twitter.app_secret";

    public static final String BOSS_APP_ID = "boss.app_id";
    public static final String BOSS_APP_SECRET = "boss.app_secret";

    public static final String FOURSQUARE_APP_ID = "foursquare.app_id";
    public static final String FOURSQUARE_APP_SECRET = "foursquare.app_secret";

    private static final Properties CONFIG;

    static {
        CONFIG = new Properties();
        try {
            CONFIG.load(Credentials.class.getResourceAsStream("/credentials.properties"));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static String getProperty(String key) {
        return (String) CONFIG.get(key);
    }

}
