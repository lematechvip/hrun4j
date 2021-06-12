package vip.lematech.httprunner4j.okhttp;

import com.alibaba.fastjson.JSON;
import org.testng.Assert;
import org.testng.annotations.Test;
import vip.lematech.httprunner4j.core.converter.ObjectConverter;
import vip.lematech.httprunner4j.entity.http.RequestEntity;
import vip.lematech.httprunner4j.entity.testcase.ApiModel;
import vip.lematech.httprunner4j.entity.testcase.TestStep;
import vip.lematech.httprunner4j.helper.LogHelper;

import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 */
public class ObjectConverterTest {
    private ObjectConverter objectConverter = new ObjectConverter();
    @Test
    public void testObjectPropertiesExtends(){
        HashMap<String, Object> targetMap = new HashMap<>();
        targetMap.put("key","target");
        List<Map<String, Object>> targetList = new ArrayList<>();
        targetList.add(targetMap);
        /**
         * 对象赋值原则：
         * 1. 非自定义对象，目标对象属性有值，即使源对象有相同属性值，也以目标对象属性为准，适用于Java非自定义对象
         * 2. 自定义对象，比如RequestEntity，会进入深度属性遍历，属性赋值参考原则1
         */
        ApiModel targetModel = ApiModel.builder()
                .baseUrl("targetUrl")
                .extract("targetExtract")
                .validate(targetList)
                .request(RequestEntity.builder()
                        .allowRedirects(false)
                        .auth("targetAuth")
                        .cookies(targetMap)
                        .files("targetFiles")
                        .json("targetJson")
                        .method("targetMethod")
                        .params(targetMap)
                        .proxy(targetMap)
                        .stream(false)
                        .url("targetUrl")
                        .writeTimeout(10)
                        .readTimeout(10)
                        .headers(targetMap)
                        .build())
                .build();
        HashMap<String, Object> sourceMap = new HashMap<>();
        sourceMap.put("key","source");

        RequestEntity sourceRequestEntity = RequestEntity.builder()
                .allowRedirects(true)
                .auth("sourceAuth")
                .cookies(sourceMap)
                .files("sourceFiles")
                .connectTimeout(20)
                .json("sourceJson")
                .method("sourceMethod")
                .params(sourceMap)
                .proxy(sourceMap)
                .stream(true)
                .url("sourceUrl")
                .writeTimeout(20)
                .readTimeout(20)
                .headers(sourceMap)
                .build();
        TestStep testStep = new TestStep();
        testStep.setRequest(sourceRequestEntity);
        testStep.setName("Name---------");
        testStep.setSetupHooks("testSetupHook");
        testStep.setTeardownHooks("teardownHooks");
        ApiModel apiResultModel = (ApiModel)objectConverter.objectsExtendsPropertyValue(targetModel,testStep);
        LogHelper.info(JSON.toJSONString(apiResultModel));
        Assert.assertEquals(apiResultModel.getName(),testStep.getName());
        Assert.assertEquals(apiResultModel.getName(),targetModel.getName());
        Assert.assertEquals(apiResultModel.getRequest().getReadTimeout(),targetModel.getRequest().getReadTimeout());
        Assert.assertEquals(apiResultModel.getRequest().getWriteTimeout(),targetModel.getRequest().getWriteTimeout());
        Assert.assertEquals(apiResultModel.getRequest().getConnectTimeout(),testStep.getRequest().getConnectTimeout());
        Assert.assertEquals(apiResultModel.getExtract(),targetModel.getExtract());
        Assert.assertEquals(apiResultModel.getValidate(),targetModel.getValidate());

        Assert.assertEquals(apiResultModel.getTeardownHooks(),testStep.getTeardownHooks());
        Assert.assertEquals(apiResultModel.getSetupHooks(),testStep.getSetupHooks());
    }
}
