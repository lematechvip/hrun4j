package vip.lematech.hrun4j.helper;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejlchina.okhttps.Download;
import com.ejlchina.okhttps.HTTP;
import com.ejlchina.okhttps.HttpResult;
import com.ejlchina.okhttps.OkHttps;
import com.ejlchina.okhttps.internal.SyncHttpTask;
import com.google.common.collect.Maps;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import vip.lematech.hrun4j.config.i18n.I18NFactory;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.common.DefinedException;
import vip.lematech.hrun4j.config.RunnerConfig;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.http.ResponseEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * website https://www.lematech.vip/
 *
 * @author lematech@foxmail.com
 * @version 1.0.1
 */

public class OkHttpsHelper {

    /**
     * Prevent request headers from containing special characters
     *
     * @param val
     * @return
     */
    private static String getValueEncoded(String val) {
        if (val == null) {
            return null;
        }
        String newValue = val.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    return URLEncoder.encode(newValue, Constant.CHARSET_UTF_8);
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
            headers.put(Constant.HEADER_COOKIE, getCookiesWithParams(cookies));
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

    /**
     * @param requestEntity request entity
     * @return ResponseEntity
     */
    public static ResponseEntity executeReq(RequestEntity requestEntity) {
        String method = requestEntity.getMethod();
        String url = requestEntity.getUrl();
        Map<String, String> headers = handleHeadersCookie(requestEntity);
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("request.url"), url));
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("request.method"), method));
        SyncHttpTask syncHttpTask = getSyncHttpTask(requestEntity, url);
        initRequestParameter(requestEntity, headers, syncHttpTask);
        HttpResult httpResult = null;
        long startTime = System.currentTimeMillis();
        if (HTTP.GET.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.get();
        } else if (HTTP.POST.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.post();
        } else if (HTTP.DELETE.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.delete();
        } else if (HTTP.PUT.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.put();
        } else if (HTTP.HEAD.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.head();
        } else if (HTTP.PATCH.equalsIgnoreCase(method)) {
            httpResult = syncHttpTask.patch();
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        if (Objects.isNull(httpResult)) {
            throw new DefinedException("The interface response information cannot be empty!");
        }
        Boolean streamObj = requestEntity.getStream();
        ResponseEntity responseEntity = wrapperResponseEntity(httpResult, streamObj, elapsedTime);
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("response.status.code"), LittleHelper.emptyIfNull(responseEntity.getStatusCode())));
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("response.body"), LittleHelper.emptyIfNull(responseEntity.getBody())));
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("response.content.length"), LittleHelper.emptyIfNull(responseEntity.getContentLength())));
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("response.time"), LittleHelper.emptyIfNull(responseEntity.getTime())));
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("response.header"), LittleHelper.emptyIfNull(JSON.toJSONString(responseEntity.getHeaders()))));
        LogHelper.info(String.format(I18NFactory.getLocaleMessage("response.cookie"), LittleHelper.emptyIfNull(responseEntity.getCookies())));
        return responseEntity;
    }

    /**
     * Initialize the request parameters
     *
     * @param requestEntity
     * @param headers
     * @param syncHttpTask
     */
    private static void initRequestParameter(RequestEntity requestEntity, Map<String, String> headers, SyncHttpTask syncHttpTask) {
        Map<String, Object> urlPara = requestEntity.getParams();
        if (MapUtil.isNotEmpty(urlPara)) {
            LogHelper.info(String.format(I18NFactory.getLocaleMessage("request.parameter"), urlPara));
            syncHttpTask.addUrlPara(urlPara);
        }
        if (MapUtil.isNotEmpty(headers)) {
            LogHelper.info(String.format(I18NFactory.getLocaleMessage("request.header"), headers));
            if (headers.containsKey(Constant.HEADER_COOKIE)) {
                LogHelper.info(String.format(I18NFactory.getLocaleMessage("request.cookie"), LittleHelper.emptyIfNull(requestEntity.getCookies())));
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                headers.put(key, getValueEncoded(value));
            }
            syncHttpTask.addHeader(headers);
        }
        Object jsonObj = requestEntity.getJson();
        if (!Objects.isNull(jsonObj)) {
            try{
                JSONObject jsonData = JSONObject.parseObject(JSONObject.toJSONString(jsonObj));
                LogHelper.info(String.format(I18NFactory.getLocaleMessage("request.json"), LittleHelper.emptyIfNull(requestEntity.getJson())));
                syncHttpTask.bodyType(OkHttps.JSON);
                syncHttpTask.setBodyPara(jsonData);
            }catch (Exception e){
                String exceptionMsg = String.format("JSON formatting exception, cause of exception: %s",e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
        }
        Object requestBody = requestEntity.getData();
        if (Objects.nonNull(requestBody)) {
            if (requestBody instanceof Map) {
                syncHttpTask.addBodyPara((Map) requestBody);
            } else if (requestBody instanceof String) {
                syncHttpTask.bodyType(OkHttps.JSON);
                String requestParam = (String) requestBody;
                syncHttpTask.setBodyPara(requestParam);
            }
        }
        Object fileObj = requestEntity.getFiles();
        if (Objects.nonNull(fileObj)) {
            if (fileObj instanceof Map&&((Map<?, ?>) fileObj).size()>0) {
                Map<String, File> files = (Map) fileObj;
                for (Map.Entry<String, File> entry : files.entrySet()) {
                    File file = entry.getValue();
                    String fileName = entry.getKey();
                    syncHttpTask.addFilePara(fileName, file);
                }
                syncHttpTask.stepRate(0.1)
                .setOnProcess((process -> {
                    LogHelper.info(I18NFactory.getLocaleMessage("file.upload.process"));
                    long totalBytes = process.getTotalBytes();
                    LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.total.bytes"),totalBytes));
                    long doneBytes = process.getDoneBytes();
                    LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.done.bytes"),doneBytes));
                    double rate = process.getRate();
                    LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.done.rate"),rate));
                    boolean isDone = process.isDone();
                    LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.is.done"),isDone));
                }));
            }

        }
    }

    /**
     * @param requestEntity
     * @param url
     * @return
     */
    private static SyncHttpTask getSyncHttpTask(RequestEntity requestEntity, String url) {
        return OkHttps.newBuilder()
                .config((OkHttpClient.Builder builder) -> {
                    try {
                        Integer connectTimeout = requestEntity.getConnectTimeout();
                        if (!Objects.isNull(connectTimeout)) {
                            builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
                        }
                        Integer writeTimeout = requestEntity.getWriteTimeout();
                        if (!Objects.isNull(writeTimeout)) {
                            builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
                        }
                        Integer readTimeout = requestEntity.getReadTimeout();
                        if (!Objects.isNull(readTimeout)) {
                            builder.readTimeout(readTimeout, TimeUnit.SECONDS);
                        }
                        builder.followRedirects(requestEntity.getAllowRedirects());
                        Map<String, Object> proxy = requestEntity.getProxy();
                        if (!Objects.isNull(proxy)) {
                            String hostname = (String) proxy.get("hostname");
                            Object port = proxy.get("port");
                            int portNumber = Integer.parseInt(String.valueOf(port));
                            InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved(hostname, portNumber);
                            builder.proxy(new Proxy(Proxy.Type.HTTP, inetSocketAddress));
                        }
                    } catch (Exception e) {
                        String exceptionMsg = String.format("An exception occurred during initialization of the OkHttps configuration. Exception message: %s", e.getMessage());
                        throw new DefinedException(exceptionMsg);
                    }
                }).build().sync(url);
    }

    /**
     * @param httpResult
     * @param stream
     * @param elapsedTime
     * @return
     */
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
                String workDir = RunnerConfig.getInstance().getWorkDirectory().getAbsolutePath();
                if (workDir.endsWith(Constant.DOT_PATH)) {
                    workDir = LittleHelper.replaceLast(workDir, Constant.DOT_PATH, "");
                }
                CountDownLatch latch = new CountDownLatch(1);
                String finalWorkDir = workDir;
                body.stepRate(0.1)
                    .setOnProcess((process -> {
                        LogHelper.info(I18NFactory.getLocaleMessage("file.download.process"));
                        long totalBytes = process.getTotalBytes();
                        LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.total.bytes"),totalBytes));
                        long doneBytes = process.getDoneBytes();
                        LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.done.bytes"),doneBytes));
                        double rate = process.getRate();
                        LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.done.rate"),rate * 100));
                        boolean isDone = process.isDone();
                        LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.is.done"),isDone));
                    }))
                    .toFolder(workDir)
                    .setOnFailure((Download.Failure failure) -> {
                        LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.failure"),failure.getException().getMessage()));
                    })
                    .setOnSuccess((File file) -> {
                        LogHelper.info(String.format(I18NFactory.getLocaleMessage("file.success"),FileUtil.normalize(finalWorkDir)));
                        latch.countDown();
                    })
                    .start();
                   try {
                        latch.await();
                    } catch (InterruptedException e) {
                       String exception = "Download interrupt exception";
                       throw new DefinedException(exception);
                    }
            } else {
                String responseContent = body.toString();
                if (JsonHelper.isJson(responseContent)) {
                    responseEntity.setBody(JSON.parseObject(responseContent));
                } else {
                    responseEntity.setBody(responseContent);
                }
            }
        }
        return responseEntity;
    }

}