package io.lematech.httprunner4j.junit.dataconstructor;

import com.googlecode.aviator.AviatorEvaluator;
import io.lematech.httprunner4j.base.TestBase;
import io.lematech.httprunner4j.core.provider.DataConstructor;
import io.lematech.httprunner4j.widget.exp.BuiltInAviatorEvaluator;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className DCTest
 * @description TODO
 * @created 2021/4/30 10:26 上午
 * @publicWechat lematech
 */
public class DCTest {
    @Test
    public void testNonAssociationParameterDataConstructor() {
        DataConstructor dataConstructor = new DataConstructor();
        Map<String, List<Object>> parameters = Maps.newHashMap();
        List<Object> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        parameters.put("optionValue", list);
        List<Map<String, Object>> result = dataConstructor.parameterized(parameters);
        MyLog.info("匹配结果：{}", result);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testNonAssociationParameterFileDataConstructor() {
        DataConstructor dataConstructor = new DataConstructor();
        Map<String, List<Object>> parameters = Maps.newHashMap();
        List<Object> list = new ArrayList<>();
        list.add("${parameterize(/Users/arkhe/Desktop/resources/data/csvFile.csv)}");
        parameters.put("optionValue", list);
        List<Map<String, Object>> result = dataConstructor.parameterized(parameters);
        MyLog.info("匹配结果：{}", result);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testFileExp() {

        String url = TestBase.class.getClassLoader().getResource("").getPath();
        MyLog.info("文件路径：{}", url);
        //  AviatorEvaluator.addFunction(new BuiltInAviatorEvaluator.BuiltInFunctionParameterize());
        //parameterize()：自定义函数
        //   System.out.println(AviatorEvaluator.execute("P(data/csvFile.csv)"));

    }


}
