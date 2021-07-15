package vip.lematech.hrun4j.okhttp;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.helper.LogHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.charset.Charset;
import java.util.HashMap;

public class OkHttpTest {
    private static final String BASE_URL = "http://127.0.0.1:8000";
    private String TOKEN;

    @BeforeClass
    public void testGetToken() {
        HashMap header = Maps.newHashMap();
        header.put("device_sn", "2021032311113");
        header.put("os_platform", "MacOS");
        header.put("app_version", "1.0.1");
        HttpResult httpResult = OkHttps.sync(BASE_URL + "/api/get-token")
                .addHeader(header)
                .bodyType(OkHttps.JSON)
                .charset(Charset.defaultCharset())
                .addBodyPara("sign", "9a43330baaa4c196345fa2882a82442a44baaa59")
                .post();
        LogHelper.info("响应状态码：{}", httpResult.getStatus());
        LogHelper.info("响应头：{}", httpResult.getHeaders());
        LogHelper.info("响应内容长度：{}", httpResult.getContentLength());
        String body = httpResult.getBody().toString();
        LogHelper.info("响应信息：{}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject dataObject = (JSONObject) jsonObject.get("data");
        Assert.assertNotNull(dataObject);
        Assert.assertNotNull(dataObject.get("token"));
        TOKEN = dataObject.get("token").toString();
    }

    @Test
    public void testCreateUser() {
        HashMap header = Maps.newHashMap();
        header.put("device_sn", "2021032311113");
        header.put("token", TOKEN);
        HttpResult httpResult = OkHttps.sync(BASE_URL + "/api/user/{uid}")
                .addHeader(header)
                .bodyType(OkHttps.JSON)
                .charset(Charset.defaultCharset())
                .addPathPara("uid", RandomUtil.randomInt())
                .addBodyPara("name", "lematech")
                .addBodyPara("password", "qweqwe123")
                .post();
        LogHelper.info("响应状态码：{}", httpResult.getStatus());
        LogHelper.info("响应头：{}", httpResult.getHeaders());
        LogHelper.info("响应内容长度：{}", httpResult.getContentLength());
        String body = httpResult.getBody().toString();
        LogHelper.info("响应信息：{}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        Object code = jsonObject.get("code");
        Assert.assertEquals(code, "00");
    }


    @Test
    public void testQueryUser() {
        HashMap header = Maps.newHashMap();
        header.put("device_sn", "2021032311113");
        header.put("token", TOKEN);
        int uid = RandomUtil.randomInt();
        HttpResult httpResult = OkHttps.sync(BASE_URL + "/api/user/{uid}")
                .addHeader(header)
                .bodyType(OkHttps.JSON)
                .charset(Charset.defaultCharset())
                .addPathPara("uid", uid)
                .addBodyPara("name", "lematech")
                .addBodyPara("password", "qweqwe123")
                .post();
        LogHelper.info("响应状态码：{}", httpResult.getStatus());
        LogHelper.info("响应头：{}", httpResult.getHeaders());
        LogHelper.info("响应内容长度：{}", httpResult.getContentLength());
        String body = httpResult.getBody().toString();
        LogHelper.info("响应信息：{}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        Object code = jsonObject.get("code");
        Assert.assertEquals(code, "00");
        httpResult = OkHttps.sync(BASE_URL + "/api/user/{uid}")
                .addHeader(header)
                .bodyType(OkHttps.JSON)
                .charset(Charset.defaultCharset())
                .addPathPara("uid", uid)
                .get();
        LogHelper.info("响应状态码：{}", httpResult.getStatus());
        LogHelper.info("响应头：{}", httpResult.getHeaders());
        LogHelper.info("响应内容长度：{}", httpResult.getContentLength());
        body = httpResult.getBody().toString();
        LogHelper.info("响应信息：{}", body);
        jsonObject = JSONObject.parseObject(body);
        code = jsonObject.get("code");
        Assert.assertEquals(code, "00");
    }


    @Test
    public void testUpdateUser() {
        HashMap header = Maps.newHashMap();
        header.put("device_sn", "2021032311113");
        header.put("token", TOKEN);
        int uid = RandomUtil.randomInt();
        HttpResult httpResult = OkHttps.sync(BASE_URL + "/api/user/{uid}")
                .addHeader(header)
                .bodyType(OkHttps.JSON)
                .charset(Charset.defaultCharset())
                .addPathPara("uid", uid)
                .addBodyPara("name", "lematech")
                .addBodyPara("password", "qweqwe123")
                .post();
        LogHelper.info("响应状态码：{}", httpResult.getStatus());
        LogHelper.info("响应头：{}", httpResult.getHeaders());
        LogHelper.info("响应内容长度：{}", httpResult.getContentLength());
        String body = httpResult.getBody().toString();
        LogHelper.info("响应信息：{}", body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        Object code = jsonObject.get("code");
        Assert.assertEquals(code, "00");
        httpResult = OkHttps.sync(BASE_URL + "/api/user/{uid}")
                .addHeader(header)
                .bodyType(OkHttps.JSON)
                .charset(Charset.defaultCharset())
                .addPathPara("uid", uid)
                .addBodyPara("password", "123456")
                .put();
        LogHelper.info("响应状态码：{}", httpResult.getStatus());
        LogHelper.info("响应头：{}", httpResult.getHeaders());
        LogHelper.info("响应内容长度：{}", httpResult.getContentLength());
        body = httpResult.getBody().toString();
        LogHelper.info("响应信息：{}", body);
        jsonObject = JSONObject.parseObject(body);
        code = jsonObject.get("code");
        Assert.assertEquals(code, "00");
    }

    @Test
    public void testArchitectureMockCall(){

    }

}
