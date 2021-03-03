package io.lematech.httprunner4j.test.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;

import java.util.Stack;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className ZFTest
 * @description TODO
 * @created 2021/2/2 3:04 下午
 * @publicWechat lematech
 */
public class ZFTest {
    @Test
    public void test(){
        String json = "{\"root\":{\"data\":{\"id\":\"c84avy7pfz40\",\"created\":1609146668319,\"text\":\"脑图生成测试用例模板\",\"expandState\":\"expand\",\"font-family\":\"andale mono\",\"font-size\":16,\"note\":null},\"children\":[{\"data\":{\"id\":\"c8b2gtmea280\",\"created\":1609833833678,\"text\":\"业务模块1\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hck9zvk0\",\"created\":1609833874909,\"text\":\"子业务模块1\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hus8cxs0\",\"created\":1609833914572,\"text\":\"测试用例名称1\",\"note\":\"# 前置条件\\n休息休息\\n# 操作步骤\\n嘻嘻嘻\\n# 预期结果\\n嘻嘻嘻\\n# 备注\\n\\n\\n\\n\\n\\n\\n\",\"priority\":1},\"children\":[]},{\"data\":{\"id\":\"c8b2i2jjgbk0\",\"created\":1609833931460,\"text\":\"测试用例名称2\",\"note\":\"```\\nTips：（*）为必填项，请勿删除**# 选项描述**\\n```\\n# 前置条件（*）\\n\\n# 操作步骤（*）\\n\\n# 预期结果（*）\\n\\n# 备注\\n\\n\\n\\n\\n\\n\\n\",\"priority\":2},\"children\":[]},{\"data\":{\"id\":\"c8b2i56e2m00\",\"created\":1609833937196,\"text\":\"测试用例名称3\",\"priority\":3,\"note\":null},\"children\":[]}]},{\"data\":{\"id\":\"c8b2hck9zvk0\",\"created\":1609833874909,\"text\":\"子业务模块1\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hus8cxs0\",\"created\":1609833914572,\"text\":\"测试用例名称1\",\"note\":\"# 前置条件（*）\\n休息休息\\n# 操作步骤（*）\\n嘻嘻嘻\\n# 预期结果（*）\\n嘻嘻嘻\\n# 备注\\n\\n\\n\\n\\n\\n\\n\",\"priority\":1},\"children\":[]},{\"data\":{\"id\":\"c8b2i2jjgbk0\",\"created\":1609833931460,\"text\":\"测试用例名称2\",\"note\":\"```\\nTips：（*）为必填项，请勿删除**# 选项描述**\\n```\\n# 前置条件（*）\\n\\n# 操作步骤（*）\\n\\n# 预期结果（*）\\n\\n# 备注\\n\\n\\n\\n\\n\\n\\n\",\"priority\":2},\"children\":[]},{\"data\":{\"id\":\"c8b2i56e2m00\",\"created\":1609833937196,\"text\":\"测试用例名称3\",\"priority\":3,\"note\":\"```\\nTips：（*）为必填项，请勿删除**# 选项描述**\\n```\\n# 前置条件（*）\\n\\n# 操作步骤（*）\\n\\n# 预期结果（*）\\n\\n# 备注\\n\\n\\n\\n\\n\\n\\n\"},\"children\":[]}]}]},{\"data\":{\"id\":\"c8b2gud6q3k0\",\"created\":1609833835298,\"text\":\"业务模块2\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hck9zvk0\",\"created\":1609833874909,\"text\":\"子业务模块2\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hus8cxs0\",\"created\":1609833914572,\"text\":\"测试用例名称1\",\"note\":\"```\\nTips：（*）为必填项，请勿删除**# 选项描述**\\n```\\n# 前置条件（*）\\n\\n# 操作步骤（*）\\n\\n# 预期结果（*）\\n\\n# 备注\\n\\n\\n\\n\\n\\n\\n\"},\"children\":[]},{\"data\":{\"id\":\"c8b2i2jjgbk0\",\"created\":1609833931460,\"text\":\"测试用例名称2\"},\"children\":[]},{\"data\":{\"id\":\"c8b2i56e2m00\",\"created\":1609833937196,\"text\":\"测试用例名称3\"},\"children\":[]},{\"data\":{\"id\":\"c8sy16mp12o0\",\"created\":1611649402948,\"text\":\"分支主题\"},\"children\":[]}]}]},{\"data\":{\"id\":\"c8b2gurbn000\",\"created\":1609833836153,\"text\":\"业务模块4\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hck9zvk0\",\"created\":1609833874909,\"text\":\"子业务模块4\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hus8cxs0\",\"created\":1609833914572,\"text\":\"测试用例名称1\",\"note\":\"```\\nTips：（*）为必填项，请勿删除**# 选项描述**\\n```\\n# 前置条件（*）\\n\\n# 操作步骤（*）\\n\\n# 预期结果（*）\\n\\n# 备注\\n\\n\\n\\n\\n\\n\\n\"},\"children\":[]},{\"data\":{\"id\":\"c8b2i2jjgbk0\",\"created\":1609833931460,\"text\":\"测试用例名称2\"},\"children\":[]},{\"data\":{\"id\":\"c8b2i56e2m00\",\"created\":1609833937196,\"text\":\"测试用例名称3\"},\"children\":[]}]}]},{\"data\":{\"id\":\"c8b2gv8olww0\",\"created\":1609833837202,\"text\":\"业务模块3\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hck9zvk0\",\"created\":1609833874909,\"text\":\"子业务模块3\",\"expandState\":\"expand\"},\"children\":[{\"data\":{\"id\":\"c8b2hus8cxs0\",\"created\":1609833914572,\"text\":\"测试用例名称1\",\"note\":\"```\\nTips：（*）为必填项，请勿删除**# 选项描述**\\n```\\n# 前置条件（*）\\n\\n# 操作步骤（*）\\n\\n# 预期结果（*）\\n\\n# 备注\\n\\n\\n\\n\\n\\n\\n\"},\"children\":[]},{\"data\":{\"id\":\"c8b2i2jjgbk0\",\"created\":1609833931460,\"text\":\"测试用例名称2\"},\"children\":[]},{\"data\":{\"id\":\"c8b2i56e2m00\",\"created\":1609833937196,\"text\":\"测试用例名称3\"},\"children\":[]}]}]}]},\"template\":\"default\",\"theme\":\"fresh-blue\",\"version\":\"1.4.43\"}";
        JSONObject caseObj = JSON.parseObject(json);
        fuckInStack = new Stack<>();
        fuckOutStack = new Stack<>();
        boolean flag = false;
        if (caseObj.getJSONObject("root").getJSONArray("children").size() != 0) {
             traverse(caseObj.getJSONObject("root"));
        }
    }

    private static boolean flag = false;
    private static  Stack<String> fuckInStack = new Stack<>();
    private static  Stack<String> fuckOutStack = new Stack<>();
    public static JSONObject traverse(JSONObject caseObj) {

            if (caseObj.getJSONArray("children").size() != 0) {
                String text = caseObj.getJSONObject("data").getString("text");
                if(flag){
                    fuckInStack.pop();
                    fuckOutStack.push(text);
                }else{
                    fuckInStack.push(text);
                }
            }
            if (caseObj.getJSONArray("children").size() == 0) {
                flag = true;
                fuckInStack.addAll(fuckOutStack);
                fuckOutStack = new Stack<>();
                String moduleName = new String();
                StringBuffer subModuleName = new StringBuffer();

                if(fuckInStack.size()==2){
                    moduleName = fuckInStack.get(0);
                    subModuleName = subModuleName.append(fuckInStack.get(1));
                }else {
                    for (int index = 1;index<fuckInStack.size();index++) {
                        String value = fuckInStack.get(index);
                        if(index == 1){
                            moduleName = value;
                        }else{
                            subModuleName.append(value);
                            if(index < fuckInStack.size()-1){
                                subModuleName.append("_");
                            }
                        }
                    }
                }
                System.out.println("用例信息："+fuckInStack);
                System.out.println("用例信息：模块："+moduleName+" 子模块："+subModuleName.toString());
              //  log.info("模块：{},子模块：{}", moduleName, subModuleName);
            }
            for (Object o : caseObj.getJSONArray("children")) {
               traverse(((JSONObject) o));

            }

        return null;
    }
}
