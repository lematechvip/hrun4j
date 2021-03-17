package io.lematech.httprunner4j.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.lematech.httprunner4j.I18NFactory;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.common.DefinedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className HttpClientUtil
 * @description TODO
 * @created 2021/1/28 4:59 下午
 * @publicWechat lematech
 */
@Slf4j
public class HttpClientUtil {
    private static final int connectTimeout = 10000;
    private static final int socketTimeout = 10000;

    public static ResponseEntity doGet(String url) {
        return doGet(url, Collections.EMPTY_MAP);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers) {
        return doGet(url, headers, Collections.EMPTY_MAP);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers, Map<String, Object> params) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        return doGet(url, headers, params, requestConfig);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers, Map<String, Object> params, RequestConfig requestConfig) {
        return doGet(url, headers, params, new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers, Map<String, Object> params, CookieStore httpCookieStore, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(Optional.ofNullable(httpCookieStore).orElse(httpCookieStore = new BasicCookieStore()))
                .build();
        String apiUrl = getUrlWithParams(url, params);
        HttpGet httpGet = new HttpGet(apiUrl);
        httpGet.setConfig(requestConfig);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = null;
        try {
            long startTime = System.currentTimeMillis();
            response = httpClient.execute(httpGet);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            return wrapperResponseEntity(response, elapsedTime, httpCookieStore);
        } catch (ClientProtocolException e) {
            throw new DefinedException("client protocol exception: " + e.getMessage());
        } catch (ParseException e) {
            throw new DefinedException("parse exception: " + e.getMessage());
        } catch (IOException e) {
            throw new DefinedException("io exception: " + e.getMessage());
        } finally {
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    log.warn("release connection exception");
                }
            }
        }
    }

    public static ResponseEntity doPost(String url) {
        return doPost(url, Collections.EMPTY_MAP);
    }

    public static ResponseEntity doPost(String url, Map<String, Object> params) {
        return doPost(url, Collections.EMPTY_MAP, params, new String());
    }

    public static ResponseEntity doPost(String url, Map<String, Object> params, String json) {
        return doPost(url, Collections.EMPTY_MAP, params, json);
    }

    public static ResponseEntity doPost(String url, Map<String, String> headers, Map<String, Object> params, String json) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        return doPost(url, headers, params, json, requestConfig);
    }

    public static ResponseEntity doPost(String url, Map<String, String> headers, Map<String, Object> params, String json, RequestConfig requestConfig) {
        return doPost(url, headers, params, json, new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doPost(String url, Map<String, String> headers, Map<String, Object> params, String json, CookieStore httpCookieStore, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(Optional.ofNullable(httpCookieStore).orElse(httpCookieStore = new BasicCookieStore()))
                .build();
        HttpPost httpPost;
        if (!StrUtil.isEmpty(json)) {
            url = getUrlWithParams(url, params);
            httpPost = new HttpPost(url);
            StringEntity jsonContent;
            try {
                jsonContent = new StringEntity(json);
                jsonContent.setContentType("application/json");
                jsonContent.setContentEncoding(Constant.CHARSET_UTF_8);
                httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            } catch (UnsupportedEncodingException e) {
                String exceptionMsg = String.format("unsupported encoding exception :%s", e.getMessage());
                throw new DefinedException(exceptionMsg);
            }
            httpPost.setEntity(jsonContent);
        } else {
            httpPost = new HttpPost(url);
            if (MapUtil.isEmpty(params)) {
                HttpEntity entityReq = getUrlEncodedFormEntity(params);
                httpPost.setEntity(entityReq);
            }
        }

        httpPost.setConfig(requestConfig);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = null;
        try {
            long startTime = System.currentTimeMillis();
            response = httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            return wrapperResponseEntity(response, elapsedTime, httpCookieStore);
        } catch (ClientProtocolException e) {
            throw new DefinedException("client protocol exception: " + e.getMessage());
        } catch (ParseException e) {
            throw new DefinedException("parse exception: " + e.getMessage());
        } catch (IOException e) {
            throw new DefinedException("io exception: " + e.getMessage());
        } finally {
            if (null != response) {
                try {
                    response.close();
                    httpClient.close();
                } catch (IOException e) {
                    log.warn("release connection exception");
                }
            }
        }
    }

    private static ResponseEntity wrapperResponseEntity(CloseableHttpResponse response
            , long elapsedTime
            , CookieStore httpCookieStore) throws IOException {
        ResponseEntity responseEntity = new ResponseEntity();
        if (response == null || response.getStatusLine() == null) {
            return responseEntity;
        }
        int statusCode = response.getStatusLine().getStatusCode();
        responseEntity.setStatusCode(statusCode);
        responseEntity.setTime((elapsedTime * 1.0) / 1000);
        HashMap<String, String> headersMap = new HashMap<>();
        Header[] headerArr = response.getAllHeaders();
        for (Header header : headerArr) {
            headersMap.put(header.getName(), header.getValue());
        }
        HashMap<String, String> cookiesMap = new HashMap<>();
        if (httpCookieStore != null) {
            List<Cookie> cookies = httpCookieStore.getCookies();
            for (Cookie cookie : cookies) {
                cookiesMap.put(cookie.getName(), cookie.getValue());
            }
            responseEntity.setCookies(cookiesMap);
        }
        responseEntity.setHeaders(headersMap);
        if (statusCode == HttpStatus.SC_OK) {
            HttpEntity entityRes = response.getEntity();
            String responseContent = EntityUtils.toString(entityRes, "UTF-8");
            if (entityRes != null) {
                Header contentType = response.getFirstHeader("Content-Type");
                if (contentType != null && contentType.getValue().contains("application/json")) {
                    if (JsonUtil.isJson(responseContent)) {
                        responseEntity.setContent(JSON.parseObject(responseContent));
                    } else {
                        String exceptionMsg = "json格式化失败：" + responseContent;
                        throw new DefinedException(exceptionMsg);
                    }
                } else {
                    responseEntity.setContent(responseContent);
                }
            }
        }
        return responseEntity;
    }

    private static String getUrlWithParams(String url, Map<String, Object> params) {
        boolean first = true;
        StringBuilder sb = new StringBuilder(url);
        if (params != null) {
            for (String key : params.keySet()) {
                char ch = '&';
                if (first == true) {
                    ch = '?';
                    first = false;
                }
                String value = params.get(key).toString();
                try {
                    String sval = URLEncoder.encode(value, "UTF-8");
                    sb.append(ch).append(key).append("=").append(sval);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return sb.toString();
    }

    private static HttpEntity getUrlEncodedFormEntity(Map<String, Object> params) {
        List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                    .getValue().toString());
            pairList.add(pair);
        }
        return new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8"));
    }

    public static ResponseEntity executeReq(RequestEntity requestEntity) {
        ResponseEntity responseEntity = null;
        String method = requestEntity.getMethod();
        String url = requestEntity.getUrl();
        Map<String, String> headers = requestEntity.getHeaders();
        Map<String, Object> params = requestEntity.getParams();
        String json = JSON.toJSONString(requestEntity.getJson());
        log.info(String.format(I18NFactory.getLocaleMessage("requestUrl"), requestEntity.getUrl()));
        log.info(String.format(I18NFactory.getLocaleMessage("requestMethod"), requestEntity.getMethod()));
        log.info(String.format(I18NFactory.getLocaleMessage("requestHeader"), requestEntity.getHeaders()));
        log.info(String.format(I18NFactory.getLocaleMessage("requestCookie"), requestEntity.getCookies()));
        log.info(String.format(I18NFactory.getLocaleMessage("requestParameter"), requestEntity.getParams()));
        log.info(String.format(I18NFactory.getLocaleMessage("requestJson"), requestEntity.getJson()));
        if ("GET".equalsIgnoreCase(requestEntity.getMethod())) {
            responseEntity = doGet(url, headers, params, null);
        } else if ("POST".equalsIgnoreCase(method)) {
            responseEntity = doPost(url, headers, params, json, null);
        } else if ("CONNECT".equalsIgnoreCase(method)) {

        } else if ("TRACE".equalsIgnoreCase(method)) {

        } else if ("PUT".equalsIgnoreCase(method)) {

        } else if ("OPTIONS".equalsIgnoreCase(method)) {

        }
        if (Objects.isNull(responseEntity)) {
            throw new DefinedException("响应信息为空！");
        }
        log.info(String.format(I18NFactory.getLocaleMessage("responseStatusCode"), responseEntity.getStatusCode()));
        log.info(String.format(I18NFactory.getLocaleMessage("responseBody"), responseEntity.getContent()));
        log.info(String.format(I18NFactory.getLocaleMessage("responseTime"), responseEntity.getTime()));
        log.info(String.format(I18NFactory.getLocaleMessage("responseHeader"), responseEntity.getHeaders()));
        log.info(String.format(I18NFactory.getLocaleMessage("responseCookie"), responseEntity.getCookies()));
        return responseEntity;
    }
}
