package com.icode.api.common.config.redis;


import java.util.HashMap;
import java.util.Map;

public class CacheExpiresMap {
    private static HashMap<String, Integer> map = new HashMap<>();

    static {
        map.put("dictionary", 50);
        map.put("logoCover", 50);
    }


    public static Map<String, Integer> get() {
        return map;
    }
}
