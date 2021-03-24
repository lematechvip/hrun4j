package io.lematech.httprunner4j.junit.aviator;

import io.lematech.httprunner4j.utils.AviatorEvaluatorUtil;
import io.lematech.httprunner4j.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className AviatorTest
 * @description TODO
 * @created 2021/1/25 11:11 上午
 * @publicWechat lematech
 */
@Slf4j
public class AviatorTest{
    @Test
    public void testAviatorExp(){
       /* String expression = "axxx/(b+c)>10";
        Expression exp =  AviatorEvaluator.compile(expression);
        //exp.getVariableNames();
        System.out.println(exp.getVariableNames());
        System.out.println(exp.getVariableFullNames());*/
        Map<String,Object> map1 = new HashMap<>();
        map1.put("a",100);
        map1.put("b",5);//`多福多寿`
        map1.put("c",5);
        //
        System.out.println(AviatorEvaluatorUtil.execute("Hello",map1));//输出：20
        System.out.println(AviatorEvaluatorUtil.execute("'Hello ' + add(a,b)",map1));//输出：20
        System.out.println(AviatorEvaluatorUtil.execute("'Hello ' + subtract(a,b)",map1));//输出：20
        System.out.println(AviatorEvaluatorUtil.execute("'Hello ' + multiply(a,b)",map1));//输出：20
        System.out.println(AviatorEvaluatorUtil.execute("'Hello ' + divide(a,b)",map1));//输出：20

    /*    System.out.println(AviatorEvaluator.execute("'/api/1000?_t= ' + multiplication(a,b)",map1));//输出：20
        System.out.println(AviatorEvaluator.execute("/api/1000?_t= '+multiplication(a,multiplication(a,multiplication(a,b)))+'&test' + multiplication(a,b)",map1));//输出：20
        System.out.println(AviatorEvaluator.execute("multiplication(10,multiplication(5,4))"));//输出：200
*/
    }


    @Test
    public void testJmesJson(){

        String jsonStr = "{\n" +
                "  \"aggregations\": {\n" +
                "    \"sales_over_time\": {\n" +
                "      \"buckets\": [{\n" +
                "        \"key_as_string\": \"2015-01-01\",\n" +
                "        \"key\": 1420070400000,\n" +
                "        \"doc_count\": 3\n" +
                "      }, {\n" +
                "        \"key_as_string\": \"2015-02-01\",\n" +
                "        \"key\": 1422748800000,\n" +
                "        \"doc_count\": 2\n" +
                "      }, {\n" +
                "        \"key_as_string\": \"2015-03-01\",\n" +
                "        \"key\": 1425168000000,\n" +
                "        \"doc_count\": 2\n" +
                "      }]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String exp = "aggregations.sales_over_time.buckets[*].{key: key_as_string, value: doc_count}";
        log.info("表达式：{},提取结果：{}", exp, JsonUtil.getJmesPathResult(exp, jsonStr));

    }
}
