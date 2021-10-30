package vip.lematech.hrun4j.config;

import vip.lematech.hrun4j.entity.testcase.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * website https://www.lematech.vip/
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class NamespaceMap {
    public static Map<String, TestCase> getNamespaceMap() {
        if (namespaceMap == null) {
            initializeEnv();
        }
        return namespaceMap;
    }

    private static Map<String, TestCase> namespaceMap;

    private static synchronized void initializeEnv() {
        if (namespaceMap == null) {
            namespaceMap = new HashMap<>();
        }
    }

    public static void setDataObject(String key, TestCase value) {
        if (namespaceMap == null) {
            initializeEnv();
        }
        namespaceMap.put(key, value);
    }

    public static TestCase getDataObject(String key) {
        TestCase value = null;
        if (namespaceMap == null) {
            initializeEnv();
        }
        if (namespaceMap.containsKey(key)) {
            value = namespaceMap.get(key);
        }
        return value;
    }
}
