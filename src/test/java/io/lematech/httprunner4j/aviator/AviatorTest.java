package io.lematech.httprunner4j.aviator;

import io.lematech.httprunner4j.utils.AviatorEvaluatorUtil;
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

}
