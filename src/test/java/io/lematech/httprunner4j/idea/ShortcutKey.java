package io.lematech.httprunner4j.idea;

import io.lematech.httprunner4j.utils.MyHttpClient;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ShortcutKey
 * @description TODO
 * @created 2021/1/22 2:38 下午
 * @publicWechat lematech
 */
public class ShortcutKey {
    @Test
    public void test1() throws Exception{
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("foo", "bar"));
            form.add(new BasicNameValuePair("employee", "maxsu"));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

            HttpPost httpPost = new HttpPost("http://httpbin.org/post");
            httpPost.setEntity(entity);
            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
        }//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/httpclient/httpclient-html-form-post.html
        Map<String,Object> formData = new HashMap<>();
        formData.put("foo", "bar");
        formData.put("employee", "maxsu");
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json;charset=UTF-8");
       // MyHttpClient.doPost("http://httpbin.org/post",formData);
        System.out.println(MyHttpClient.doPost("http://httpbin.org/post",headers,formData));
        String jsonStr = "{\"code\":\"00\",\"msg\":\"ok！\",\"data\":[{\"testStatus\":3,\"count\":13},{\"testStatus\":4,\"count\":22},{\"testStatus\":5,\"count\":27}]}";
        System.out.println(MyHttpClient.doPostJson("http://httpbin.org/post",jsonStr));
    }

}
