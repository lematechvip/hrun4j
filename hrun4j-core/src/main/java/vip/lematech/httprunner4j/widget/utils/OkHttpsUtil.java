package vip.lematech.httprunner4j.widget.utils;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.ejlchina.okhttps.internal.SyncHttpTask;

import com.google.common.collect.Maps;
import vip.lematech.httprunner4j.common.Constant;
import vip.lematech.httprunner4j.common.DefinedException;
import vip.lematech.httprunner4j.config.RunnerConfig;
import vip.lematech.httprunner4j.entity.http.HttpConstant;
import vip.lematech.httprunner4j.entity.http.RequestEntity;
import vip.lematech.httprunner4j.entity.http.ResponseEntity;
import vip.lematech.httprunner4j.widget.i18n.I18NFactory;
import vip.lematech.httprunner4j.widget.log.MyLog;

import okhttp3.Headers;
import okhttp3.OkHttpClient;

import java.io.File;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OkHttpsUtil {
    private static String getValueEncoded(String val) {
        if (val == null) {
            return "null";
        }
        ;
        String newValue = val.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return newValue;
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

    public static ResponseEntity executeReq(RequestEntity requestEntity) {
        String method = requestEntity.getMethod();
        String url = requestEntity.getUrl();
        Map<String, String> headers = handleHeadersCookie(requestEntity);
        MyLog.info(String.format(I18NFactory.getLocaleMessage("request.url"), url));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("request.method"), method));
        SyncHttpTask syncHttpTask = OkHttps.newBuilder()
                .config((OkHttpClient.Builder builder) -> {
                    Integer connectionRequestTimeout = requestEntity.getConnectionRequestTimeout();
                    if (!Objects.isNull(connectionRequestTimeout)) {
                        builder.connectTimeout(connectionRequestTimeout, TimeUnit.SECONDS);
                    }
                    Integer connectTimeout = requestEntity.getConnectTimeout();
                    if (!Objects.isNull(connectTimeout)) {
                        builder.writeTimeout(connectTimeout, TimeUnit.SECONDS);
                    }
                    Integer socketTimeout = requestEntity.getSocketTimeout();
                    if (!Objects.isNull(socketTimeout)) {
                        builder.readTimeout(socketTimeout, TimeUnit.SECONDS);
                    }
                    builder.followRedirects(requestEntity.getAllowRedirects());

                    try {
                        Map<String, Object> proxy = requestEntity.getProxy();
                        if (!Objects.isNull(proxy)) {
                            String hostname = (String) proxy.get("hostname");
                            Object port = proxy.get("port");
                            int portNumber = Integer.parseInt(String.valueOf(port));
                            InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(hostname, portNumber);
                            builder.proxy(new Proxy(Proxy.Type.HTTP, inetSocketAddress));
                        }
                    } catch (Exception e) {
                        String exceptionMsg = String.format("proxy %s info error，please check it", requestEntity.getProxy());
                        throw new DefinedException(exceptionMsg);
                    }
                }).build().sync(url);
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
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                headers.put(key, getValueEncoded(value));
            }
            syncHttpTask.addHeader(headers);
        }
        JSONObject json = requestEntity.getJson();
        if (!Objects.isNull(json)) {
            MyLog.info(String.format(I18NFactory.getLocaleMessage("request.json"), SmallUtil.emptyIfNull(requestEntity.getJson())));
            syncHttpTask.bodyType(OkHttps.JSON);
            syncHttpTask.setBodyPara(json);
        }
        Object requestBody = requestEntity.getData();
        if (Objects.nonNull(requestBody)) {
            if (requestBody instanceof Map) {
                syncHttpTask.addBodyPara((Map) requestBody);
            } else if (requestBody instanceof String) {
                String requestParam = (String) requestBody;
                syncHttpTask.bodyType(OkHttps.FORM);
                String[] reqParamsMap = requestParam.split("&");
                for (String reqParam : reqParamsMap) {
                    String[] reqParamMapValue = reqParam.split("=");
                    if (reqParamMapValue.length == 2) {
                        syncHttpTask.addBodyPara(reqParamMapValue[0], reqParamMapValue[1]);
                    }
                }
            }
        }
        Object fileObj = requestEntity.getFiles();
        Boolean streamObj = requestEntity.getStream();
        if (Objects.nonNull(fileObj)) {
            if (fileObj instanceof Map) {
                Map<String, File> files = (Map) fileObj;
                for (Map.Entry<String, File> entry : files.entrySet()) {
                    File file = entry.getValue();
                    String fileName = entry.getKey();
                    syncHttpTask.addFilePara(fileName, file);
                }
            }

        }
        HttpResult httpResult = null;
        long startTime = System.currentTimeMillis();
        if (HttpConstant.GET.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.get();
        } else if (HttpConstant.POST.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.post();
        } else if (HttpConstant.DELETE.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.delete();
        } else if (HttpConstant.PUT.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.put();
        } else if (HttpConstant.HEAD.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.head();
        } else if (HttpConstant.PATCH.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.patch();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        if (Objects.isNull(httpResult)) {
            throw new DefinedException("The interface response information cannot be empty!");
        }
        ResponseEntity responseEntity = wrapperResponseEntity(httpResult, streamObj, elapsedTime);
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.status.code"), SmallUtil.emptyIfNull(responseEntity.getStatusCode())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.body"), SmallUtil.emptyIfNull(responseEntity.getBody())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.content.length"), SmallUtil.emptyIfNull(responseEntity.getContentLength())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.time"), SmallUtil.emptyIfNull(responseEntity.getTime())));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.header"), SmallUtil.emptyIfNull(JSON.toJSONString(responseEntity.getHeaders()))));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("response.cookie"), SmallUtil.emptyIfNull(responseEntity.getCookies())));
        return responseEntity;
    }

    private static ResponseEntity wrapperResponseEntity(HttpResult httpResult
            , boolean stream, long elapsedTime) {
        ResponseEntity responseEntity = new ResponseEntity();
        if (httpResult.isSuccessful()) {
            int statusCode = httpResult.getStatus();
            responseEntity.setStatusCode(statusCode);
            responseEntity.setContentLength(httpResult.getContentLength());
            responseEntity.setTime((elapsedTime * 1.0) / 1000);
            HashMap<String, Object> headersMap = new HashMap<>();
            Headers headerArr = httpResult.getHeaders();
            Set<String> names = headerArr.names();
            for (String name : names) {
                headersMap.put(name, headerArr.get(name));
            }
            responseEntity.setHeaders(headersMap);
            HttpResult.Body body = httpResult.getBody();
            if (!stream) {
                body.toFolder(RunnerConfig.getInstance().getWorkDirectory())
                        .start();
            } else {
                String responseContent = body.toString();
                if (JsonUtil.isJson(responseContent)) {
                    responseEntity.setBody(JSON.parseObject(responseContent));
                } else {
                    responseEntity.setBody(responseContent);
                }
            }
        }
        return responseEntity;
    }

}
