package io.lematech.httprunner4j.junit.regexp;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className Json2Map
 * @description TODO
 * @created 2021/2/2 10:38 上午
 * @publicWechat lematech
 */
public class Json2Map {
    /**
     * 将json字符串转为Map结构
     * 如果json复杂，结果可能是map嵌套map
     *
     * @param jsonStr 入参，json格式字符串
     * @return 返回一个map
     */
    public static Map<String, Object> json2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<>();
        if (jsonStr != null && !"".equals(jsonStr)) {
            //最外层解析
            JSONObject json = JSONObject.fromObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //如果内层还是数组的话，继续解析
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<JSONObject> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = it.next();
                        list.add(json2Map(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        } else {
            return null;
        }
    }

    /**
     * 将json对象转换为HashMap
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> parseJSON2Map(JSONObject json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是json数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else if (v instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k.toString(), parseJSON2Map((JSONObject) v));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                map.put(k.toString(), v);
            }
        }
        return map;

    }
}
