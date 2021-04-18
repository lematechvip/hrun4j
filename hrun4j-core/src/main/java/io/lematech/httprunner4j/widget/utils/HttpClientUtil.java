package io.lematech.httprunner4j.widget.utils;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.lematech.httprunner4j.common.DefinedException;
import io.lematech.httprunner4j.entity.http.HttpConstant;
import io.lematech.httprunner4j.entity.http.RequestEntity;
import io.lematech.httprunner4j.entity.http.RequestParameterEntity;
import io.lematech.httprunner4j.entity.http.ResponseEntity;
import io.lematech.httprunner4j.widget.i18n.I18NFactory;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className HttpClientUtil
 * @description TODO
 * @created 2021/1/28 4:59 下午
 * @publicWechat lematech
 */
public class HttpClientUtil {
    public static ResponseEntity doGet(String url) {
        return doGet(url, Collections.EMPTY_MAP);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers) {
        return doGet(url, headers, new RequestParameterEntity());
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers, RequestParameterEntity requestParams) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(HttpConstant.CONNECT_TIME_OUT)
                .setSocketTimeout(HttpConstant.SOCKET_TIME_OUT)
                .build();
        return doGet(url, headers, requestParams, requestConfig);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers, RequestParameterEntity requestParams, RequestConfig requestConfig) {
        return doGet(url, headers, requestParams, new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doGet(String url, Map<String, String> headers, RequestParameterEntity requestParams, CookieStore httpCookieStore, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(Optional.ofNullable(httpCookieStore).orElse(httpCookieStore = new BasicCookieStore()))
                .build();
        String apiUrl = getUrlWithParams(url, requestParams.getParams());
        HttpGetWithEntity httpGet = new HttpGetWithEntity(apiUrl);
        httpGet.setConfig(requestConfig);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        setRequestBody(httpGet, requestParams);
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
                    MyLog.warn("release connection exception");
                }
            }
        }
    }


    public static ResponseEntity doPost(String url) {
        return doPost(url, new RequestParameterEntity());
    }

    public static ResponseEntity doPost(String url, RequestParameterEntity requestParams) {
        return doPost(url, Collections.EMPTY_MAP, requestParams);
    }

    public static ResponseEntity doPost(String url, Map<String, String> headers, RequestParameterEntity requestParams) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(HttpConstant.CONNECT_TIME_OUT)
                .setSocketTimeout(HttpConstant.SOCKET_TIME_OUT)
                .build();
        return doPost(url, headers, requestParams, requestConfig);
    }

    public static ResponseEntity doPost(String url, Map<String, String> headers, RequestParameterEntity requestParams, RequestConfig requestConfig) {
        return doPost(url, headers, requestParams, new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doPost(String url, Map<String, String> headers, RequestParameterEntity requestParams, CookieStore httpCookieStore, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(Optional.ofNullable(httpCookieStore).orElse(httpCookieStore = new BasicCookieStore()))
                .build();
        HttpPost httpPost;
        url = getUrlWithParams(url, requestParams.getParams());
        httpPost = new HttpPost(url);
        setRequestBody(httpPost, requestParams);
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
                    MyLog.warn("release connection exception");
                }
            }
        }
    }

    public static ResponseEntity doDelete(String url, Map<String, String> headers, RequestConfig requestConfig) {
        return doDelete(url, headers, new RequestParameterEntity(), new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doDelete(String url, Map<String, String> headers, RequestParameterEntity requestParams, RequestConfig requestConfig) {
        return doDelete(url, headers, requestParams, new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doDelete(String url, Map<String, String> headers, RequestParameterEntity requestParams, CookieStore httpCookieStore, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(Optional.ofNullable(httpCookieStore).orElse(httpCookieStore = new BasicCookieStore()))
                .build();
        url = getUrlWithParams(url, requestParams.getParams());
        HttpDeleteWithEntity httpDelete = new HttpDeleteWithEntity(url);
        httpDelete.setConfig(requestConfig);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpDelete.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = null;
        setRequestBody(httpDelete, requestParams);
        try {
            long startTime = System.currentTimeMillis();
            response = httpClient.execute(httpDelete);
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
                    MyLog.warn("release connection exception");
                }
            }
        }
    }


    public static ResponseEntity doPut(String url, Map<String, String> headers, RequestParameterEntity requestParams, RequestConfig requestConfig) {
        return doPut(url, headers, requestParams, new BasicCookieStore(), requestConfig);
    }

    public static ResponseEntity doPut(String url, Map<String, String> headers, RequestParameterEntity requestParams, CookieStore httpCookieStore, RequestConfig requestConfig) {
        CloseableHttpClient httpClient = HttpClients
                .custom()
                .setDefaultCookieStore(Optional.ofNullable(httpCookieStore).orElse(httpCookieStore = new BasicCookieStore()))
                .build();
        HttpPut httpPut;
        url = getUrlWithParams(url, requestParams.getParams());
        httpPut = new HttpPut(url);
        setRequestBody(httpPut, requestParams);
        httpPut.setConfig(requestConfig);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPut.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = null;
        try {
            long startTime = System.currentTimeMillis();
            response = httpClient.execute(httpPut);
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
                    MyLog.warn("release connection exception");
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
        responseEntity.setContentLength(response.getEntity().getContentLength());
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
        return new UrlEncodedFormEntity(pairList, StandardCharsets.UTF_8);
    }

    private static void setRequestBody(HttpEntityEnclosingRequestBase request, RequestParameterEntity requestParams) {
        if (Objects.nonNull(requestParams.getData())) {
            if (requestParams.getData() instanceof JSONObject) {
                Map<String, Object> dataMap = JSONObject.parseObject(JSON.toJSONString(requestParams.getData()), Map.class);
                HttpEntity entityReq = getUrlEncodedFormEntity(dataMap);
                request.setEntity(entityReq);
            }
        }
        if (Objects.nonNull(requestParams.getJson())) {
            HttpEntity httpEntity = new StringEntity(JSON.toJSONString(requestParams.getJson()), ContentType.APPLICATION_JSON);
            request.setEntity(httpEntity);
        }
    }

    /**
     * @param requestEntity
     * @return
     */
    private static RequestConfig initRequestConfig(RequestEntity requestEntity) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .build();
        RequestConfig.Builder builder = RequestConfig.copy(defaultRequestConfig)
                .setConnectTimeout(HttpConstant.CONNECT_TIME_OUT)
                .setConnectionRequestTimeout(HttpConstant.CONNECTION_REQUEST_TIME_OUT)
                .setSocketTimeout(HttpConstant.SOCKET_TIME_OUT);
        Integer connectionRequestTimeout = requestEntity.getConnectionRequestTimeout();
        if (!Objects.isNull(connectionRequestTimeout)) {
            builder.setConnectionRequestTimeout(connectionRequestTimeout);
        }
        Integer connectTimeout = requestEntity.getConnectTimeout();
        if (!Objects.isNull(connectTimeout)) {
            builder.setConnectTimeout(connectTimeout);
        }
        Integer socketTimeout = requestEntity.getSocketTimeout();
        if (!Objects.isNull(socketTimeout)) {
            builder.setSocketTimeout(socketTimeout);
        }
        Boolean allowRedirects = requestEntity.getAllowRedirects();
        if (!Objects.isNull(allowRedirects)) {
            builder.setRedirectsEnabled(allowRedirects);
        }
        try {
            Map<String, Object> proxy = requestEntity.getProxy();
            if (!Objects.isNull(proxy)) {
                String hostname = (String) proxy.get("hostname");
                Object port = proxy.get("port");
                HttpHost httpHost;
                if (Objects.isNull(port)) {
                    httpHost = new HttpHost(hostname);
                } else {
                    int portNumber = Integer.parseInt(String.valueOf(port));
                    httpHost = new HttpHost(hostname, portNumber);
                }
                builder.setProxy(httpHost);
            }
        } catch (Exception e) {
            String exceptionMsg = String.format("proxy %s info error，please check it", requestEntity.getProxy());
            throw new DefinedException(exceptionMsg);
        }
        return builder.build();
    }

    public static ResponseEntity executeReq(RequestEntity requestEntity) {
        ResponseEntity responseEntity = null;
        String method = requestEntity.getMethod();
        String url = requestEntity.getUrl();
        Map<String, String> headers = requestEntity.getHeaders();
        RequestParameterEntity requestParameterEntity = new RequestParameterEntity();
        BeanUtil.copyProperties(requestEntity, requestParameterEntity);
        MyLog.info(String.format(I18NFactory.getLocaleMessage("requestUrl"), requestEntity.getUrl()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("requestMethod"), requestEntity.getMethod()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("requestHeader"), requestEntity.getHeaders()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("requestCookie"), requestEntity.getCookies()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("requestParameter"), requestEntity.getParams()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("requestJson"), requestEntity.getJson()));
        if (HttpConstant.GET.equalsIgnoreCase(requestEntity.getMethod())) {
            responseEntity = doGet(url, headers, requestParameterEntity, initRequestConfig(requestEntity));
        } else if (HttpConstant.POST.equalsIgnoreCase(method)) {
            responseEntity = doPost(url, headers, requestParameterEntity, initRequestConfig(requestEntity));
        } else if (HttpConstant.DELETE.equalsIgnoreCase(method)) {
            responseEntity = doDelete(url, headers, requestParameterEntity, initRequestConfig(requestEntity));
        } else if (HttpConstant.PUT.equalsIgnoreCase(method)) {
            responseEntity = doPut(url, headers, requestParameterEntity, initRequestConfig(requestEntity));
        } else if (HttpConstant.HEAD.equalsIgnoreCase(method)) {
        } else if (HttpConstant.OPTIONS.equalsIgnoreCase(method)) {
        }
        if (Objects.isNull(responseEntity)) {
            throw new DefinedException("响应信息为空！");
        }
        MyLog.info(String.format(I18NFactory.getLocaleMessage("responseStatusCode"), responseEntity.getStatusCode()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("responseBody"), responseEntity.getContent()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("responseTime"), responseEntity.getTime()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("responseHeader"), responseEntity.getHeaders()));
        MyLog.info(String.format(I18NFactory.getLocaleMessage("responseCookie"), responseEntity.getCookies()));

        return responseEntity;
    }
}

class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    private final static String METHOD_NAME = "GET";

    public HttpGetWithEntity() {
        super();
    }

    public HttpGetWithEntity(final URI uri) {
        super();
        setURI(uri);
    }

    HttpGetWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}

class HttpDeleteWithEntity extends HttpEntityEnclosingRequestBase {
    private final static String METHOD_NAME = "DELETE";

    public HttpDeleteWithEntity() {
        super();
    }

    public HttpDeleteWithEntity(final URI uri) {
        super();
        setURI(uri);
    }

    HttpDeleteWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

}