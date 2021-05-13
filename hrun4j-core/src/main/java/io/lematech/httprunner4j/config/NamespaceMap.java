package io.lematech.httprunner4j.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className NamespaceMap
 * @description TODO
 * @created 2021/5/13 6:51 下午
 * @publicWechat lematech
 */
public class NamespaceMap {
    public static Map<String, Object> getNamespaceMap() {
        if (namespaceMap == null) {
            initializeEnv();
        }
        return namespaceMap;
    }

    private static Map<String, Object> namespaceMap;

    private static synchronized void initializeEnv() {
        if (namespaceMap == null) {
            namespaceMap = new HashMap<>();
        }
    }

    public static void setEnv(String key, Object value) {
        if (namespaceMap == null) {
            initializeEnv();
        }
        namespaceMap.put(key, value);
    }

    public static Object getEnv(String key) {
        Object value = null;
        if (namespaceMap == null) {
            initializeEnv();
        }
        if (namespaceMap.containsKey(key)) {
            value = namespaceMap.get(key);
        }
        return value;
    }
}
