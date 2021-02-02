package io.lematech.httprunner4j.regexp;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className JsonPathParserTest
 * @description TODO
 * @created 2021/2/2 9:46 上午
 * @publicWechat lematech
 */
@Slf4j
public class JsonPathParserTest {
    @Test
    public void testRegexpMethod(){
       /*MultiTree tree = new MultiTree();
        List<String> path1 = tree.listAllPathByRecursion();
        System.out.println(path1);*/

        String json = "{\"root\":{\"data\":{\"id\":\"c8s3k7ank080\",\"created\":1611563438909,\"text\":\"中心主题\"},\"children\":[{\"data\":{\"id\":\"c8s3l6bz5p40\",\"created\":1611563515176,\"text\":\"分支主题1\",\"priority\":1,\"note\":null},\"children\":[{\"data\":{\"id\":\"c8s3l7k6gxk0\",\"created\":1611563517849,\"text\":\"分支主题5\",\"note\":\"# 前置条件\\n前置条件 \\n# 操作步骤\\n操作步骤\\n# 预期结果\\n预期结果\\n# 备注\\n备注\"},\"children\":[]}]},{\"data\":{\"id\":\"c8s3l8ma2cw0\",\"created\":1611563520153,\"text\":\"分支主题2\"},\"children\":[]},{\"data\":{\"id\":\"c8s3l9ahhdk0\",\"created\":1611563521616,\"text\":\"分支主题4\"},\"children\":[]},{\"data\":{\"id\":\"c8s3la7lmg00\",\"created\":1611563523618,\"text\":\"分支主题3\"},\"children\":[]}]},\"template\":\"default\",\"theme\":\"fresh-blue\",\"version\":\"1.4.43\"}";
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(json);
        HashMap<String, Object> res = Json2Map.parseJSON2Map(jsonObject);
        log.info("结果：{}",res);
        MultiJSONTree tree = new MultiJSONTree(res);
        List<String> path1 = tree.listAllPathByRecursion();
        System.out.println(path1);
    }

}
