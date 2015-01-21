package com.yahoo.hack.server.delivery;

import javapns.Push;

/**
 * @since 10/15/11
 */
public class IosNotifier {

    private static final String[] TOKENS = {
            "3f4725e840bbfa3f0f3e06ac2a1263c328b3257a6d3c497d30d3de756efe1345", //iPhone
            "b638d7e1c5159d39d7e455e07874cd39c9eff9bbc12da35616fbf1f940815260", //iPad
    };
    private static final String KEYSTORE = "../ios/cert/Certificates.p12";
    private static final String PASSWORD = "tester";
    private static final boolean PRODUCTION = false;

    public static void combined(String msg, int badges, String sound) {
        badges = (badges <= 0 ? 0 : badges);
        sound = (sound == null ? "" : sound.trim());
        sound = (sound.length() == 0 ? "default" : sound);
        Push.combined(msg, badges, sound, KEYSTORE, PASSWORD, PRODUCTION, TOKENS);
    }

    public static void main(String... args) throws Exception {
        String msg = "Hack Yahoo";
//        msg = "RCB 162/3 in 16 overs, RR: 10.12, Chinnaswami Bangalore";
        combined(msg, 1, null);
    }


}
