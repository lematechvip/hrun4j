package io.lematech.httprunner4j.widget.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.OkHttps;
import com.ejlchina.okhttps.internal.SyncHttpTask;
import com.google.common.collect.Maps;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.RequestParameterEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.widget.i18n.I18NFactory;
import io.lematech.httprunner4j.widget.log.MyLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

public class OkHttpsUtil {
    public static ResponseEntity executeReq(RequestEntity requestEntity) {
        ResponseEntity responseEntity = null;
        String method = requestEntity.getMethod();
        String url = requestEntity.getUrl();
        Map<String, String> headers = handleHeadersCookie(requestEntity);
        RequestParameterEntity requestParameterEntity = new RequestParameterEntity();
        BeanUtil.copyProperties(requestEntity, requestParameterEntity);
        MyLog.info(String.format(I18NFactory.getLocaleMessage("request.url"), url));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("request.method"), method));
        SyncHttpTask syncHttpTask = OkHttps.sync(url);
        Map<String, Object> urlPara = requestEntity.getParams();
        if (MapUtil.isNotEmpty(urlPara)) {
            MyLog.info(String.format(I18NFactory.getLocaleMessage("request.parameter"), urlPara));
            syncHttpTask.addUrlPara(urlPara);
        }
        if (MapUtil.isNotEmpty(headers)) {
            MyLog.info(String.format(I18NFactory.getLocaleMessage("request.header"), headers));
            if (headers.containsKey("Cookie")) {
                MyLog.info(String.format(I18NFactory.getLocaleMessage("request.cookie"), SmallUtil.emptyIfNull(requestEntity.getCookies())));
            }
            syncHttpTask.addHeader(headers);
        }

        JSONObject json = requestEntity.getJson();
        if (!Objects.isNull(json)) {
            MyLog.info(String.format(I18NFactory.getLocaleMessage("request.json"), SmallUtil.emptyIfNull(requestEntity.getJson())));
            syncHttpTask.setBodyPara(json);
        }

        requestEntity.getData();


        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.status.code"), SmallUtil.emptyIfNull(responseEntity.getStatusCode())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.body"), SmallUtil.emptyIfNull(responseEntity.getBody())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.content.length"), SmallUtil.emptyIfNull(responseEntity.getContentLength())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.time"), SmallUtil.emptyIfNull(responseEntity.getTime())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.header"), SmallUtil.emptyIfNull(JSON.toJSONString(responseEntity.getHeaders()))));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.cookie"), SmallUtil.emptyIfNull(responseEntity.getCookies())));
        return responseEntity;
    }

    /**
     * @param requestEntity
     * @return
     */
    private static Map<String, String> handleHeadersCookie(RequestEntity requestEntity) {
        Map<String, String> headers = requestEntity.getHeaders();
        Map<String, String> cookies = requestEntity.getCookies();
        if (MapUtil.isEmpty(headers)) {
            headers = Maps.newConcurrentMap();
        }
        if (!Objects.isNull(cookies)) {
            headers.put("Cookie", getCookiesWithParams(cookies));
        }
        return headers;
    }

    /**
     * @param cookies
     * @return
     */
    private static String getCookiesWithParams(Map<String, String> cookies) {
        StringBuilder sb = new StringBuilder();
        if (MapUtil.isNotEmpty(cookies)) {
            for (String key : cookies.keySet()) {
                String value = cookies.get(key);
                try {
                    String sVal = URLEncoder.encode(value, Constant.CHARSET_UTF_8);
                    sb.append(key).append("=").append(sVal).append(";");
                } catch (UnsupportedEncodingException e) {
                    String exceptionMsg = String.format("Parameter %s encoding exception, exception information::%s", value, e.getMessage());
                    throw new DefinedException(exceptionMsg);
                }
            }
        }
        return sb.toString();
    }
}
