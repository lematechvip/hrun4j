package vip.lematech.hrun4j.okhttp;

import com.alibaba.fastjson.JSON;
import org.testng.Assert;
import org.testng.annotations.Test;
import vip.lematech.hrun4j.core.converter.ObjectConverter;
import vip.lematech.hrun4j.entity.http.RequestEntity;
import vip.lematech.hrun4j.entity.testcase.ApiModel;
import vip.lematech.hrun4j.entity.testcase.TestStep;
import vip.lematech.hrun4j.helper.LogHelper;

import java.util.*;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class ObjectConverterTest {
    @Test
    public void testObjectPropertiesExtends(){

        LogHelper.info("输出日志：{}",UUID.randomUUID(),toString());

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
                .headers(sourceMap)
                .build();
        TestStep sourceTestStep = new TestStep();
        sourceTestStep.setRequest(sourceRequestEntity);
        sourceTestStep.setName("Name---------");
        sourceTestStep.setSetupHooks("testSetupHook");
        sourceTestStep.setTeardownHooks("teardownHooks");
        LogHelper.info("源对象：{}",JSON.toJSONString(sourceTestStep));
        TestStep targetStep = ObjectConverter.apiModel2TestStep(targetModel);
        LogHelper.info("目标：{}",JSON.toJSONString(targetStep));
        TestStep apiResultModel = (TestStep)ObjectConverter.objectsExtendsPropertyValue(targetStep,sourceTestStep);
        LogHelper.info("结果：{}",JSON.toJSONString(apiResultModel));
        Assert.assertEquals(apiResultModel.getName(),sourceTestStep.getName());
        Assert.assertEquals(apiResultModel.getRequest().getReadTimeout(), targetStep.getRequest().getReadTimeout());
        Assert.assertEquals(apiResultModel.getRequest().getWriteTimeout(),sourceTestStep.getRequest().getWriteTimeout());
        Assert.assertEquals(apiResultModel.getRequest().getConnectTimeout(),sourceTestStep.getRequest().getConnectTimeout());
        Assert.assertEquals(apiResultModel.getExtract(),targetStep.getExtract());
        Assert.assertEquals(apiResultModel.getValidate(),targetStep.getValidate());
        Assert.assertEquals(apiResultModel.getTeardownHooks(),sourceTestStep.getTeardownHooks());
        Assert.assertEquals(apiResultModel.getSetupHooks(),sourceTestStep.getSetupHooks());
    }
}
