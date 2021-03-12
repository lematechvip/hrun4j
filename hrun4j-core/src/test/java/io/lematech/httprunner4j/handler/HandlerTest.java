package io.lematech.httprunner4j.handler;

import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.utils.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className HandlerTest
 * @description TODO
 * @created 2021/1/20 6:45 下午
 * @publicWechat lematech
 */

public class HandlerTest {
   // private Handler handler = new Handler();
    @Test
    public void testYamlLoad(){
        Properties props = System.getProperties();
        props.list(System.out);
        System.out.print("-----------------");
        Map map = System.getenv();
        Iterator it = map.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            System.out.print(entry.getKey()+"=");
            System.out.println(entry.getValue());
        }

        //handler.load("demo_testcase_request.yml");
    }
    @Test
    public void testExecutor(){
        String json = "{\"root\":{\"data\":{\"id\":\"c8s3k7ank080\",\"created\":1611563438909,\"text\":\"中心主题\"},\"children\":[{\"data\":{\"id\":\"c8s3l6bz5p40\",\"created\":1611563515176,\"text\":\"分支主题1\",\"priority\":1,\"note\":null},\"children\":[{\"data\":{\"id\":\"c8s3l7k6gxk0\",\"created\":1611563517849,\"text\":\"分支主题5\",\"note\":\"# 前置条件\\n前置条件 \\n# 操作步骤\\n操作步骤\\n# 预期结果\\n预期结果\\n# 备注\\n备注\"},\"children\":[]}]},{\"data\":{\"id\":\"c8s3l8ma2cw0\",\"created\":1611563520153,\"text\":\"分支主题2\"},\"children\":[]},{\"data\":{\"id\":\"c8s3l9ahhdk0\",\"created\":1611563521616,\"text\":\"分支主题4\"},\"children\":[]},{\"data\":{\"id\":\"c8s3la7lmg00\",\"created\":1611563523618,\"text\":\"分支主题3\"},\"children\":[]}]},\"template\":\"default\",\"theme\":\"fresh-blue\",\"version\":\"1.4.43\"}";
        JSONObject jsonData = JSONObject.fromObject(json);
        Map<String, String> resMap = new HashMap<>();
        analysisJson(jsonData, "", resMap);
        System.out.println();
        for (String str : resMap.keySet()) {
            System.out.println(str + ": " + resMap.get(str));
        }

    }
    public static void analysisJson(Object objJson, String path,
                                    Map<String, String> map) {
        // 如果obj为json数组
        if (objJson instanceof JSONArray) {
            JSONArray objArray = (JSONArray) objJson;
            for (int i = 0; i < objArray.size(); i++) {
                analysisJson(objArray.get(i), path, map);
            }
        } else if (objJson instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) objJson;
            Iterator it = jsonObject.keys();
            while (it.hasNext()) {
                String key = it.next().toString();
                Object object = jsonObject.get(key);
                if (path != "" && path != null && !"".equals(path)) {
                    path += "." + key;
                } else {
                    path += key;
                }
                if (object != null && !JSONNull.getInstance().equals(object)) {
                    // 如果得到的是数组
                    if (object instanceof JSONArray) {
                        JSONArray objArray = (JSONArray) object;
                        analysisJson(objArray, path, map);
                        path = modifyPath(path);
                    } else if (object instanceof JSONObject) {
                        analysisJson((JSONObject) object, path, map);
                        path = modifyPath(path);
                    } else {
                        map.put(path, object.toString());
                        // System.out.println("["+path+"]:"+object.toString()+" ");
                        path = modifyPath(path);
                    }
                } else {
                    map.put(path, null);
                    // System.out.println("["+path+"]:"+"null");
                    path = modifyPath(path);
                }
            }
        }
    }

    private static String modifyPath(String path) {
        if (path.indexOf('.') == -1) {
            return "";
        } else {
            return path.substring(0, path.lastIndexOf('.'));
        }
    }

    @Test
    public  void testHttpClient(){
        System.out.println(JSON.toJSONString(HttpClientUtil.doGet("https://postman-echo.com/get")));
    }
}
