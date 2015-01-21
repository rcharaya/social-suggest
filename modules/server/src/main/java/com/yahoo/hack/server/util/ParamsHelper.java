package com.yahoo.hack.server.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 10/5/11
 */
public class ParamsHelper {

    public static Map<String, String> toMap(Object... args) {
        Map<String, String>  map = new LinkedHashMap<String, String>();
        for (int i = 1; i < args.length; i += 2) {
            Object key = args[i - 1];
            Object val = args[i];
            map.put("" + key, "" + val);
        }
        return map;
    }

}
