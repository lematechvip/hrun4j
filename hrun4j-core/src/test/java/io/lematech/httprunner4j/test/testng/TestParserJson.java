package io.lematech.httprunner4j.test.testng;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestParserJson
 * @description TODO
 * @created 2021/2/24 7:58 下午
 * @publicWechat lematech
 */
public class TestParserJson {
    @Test
    public void testJson(){
        String jsonStr = "[\n" +
                "  {\n" +
                "    \"children\": [],\n" +
                "    \"disable\": true,\n" +
                "    \"id\": 1,\n" +
                "    \"label\": \"公共用例\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"id\": 1569320001625,\n" +
                "        \"label\": \"融资\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"id\": 1609750794767,\n" +
                "            \"label\": \"直贷融资\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1609750807026,\n" +
                "            \"label\": \"融资担保\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1609750829934,\n" +
                "            \"label\": \"结构化融资\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1611910207039,\n" +
                "            \"label\": \"入池匹配\",\n" +
                "            \"children\": []\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1609746583321,\n" +
                "        \"label\": \"交易\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"id\": 1610614190735,\n" +
                "            \"label\": \"订单\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1610614211895,\n" +
                "            \"label\": \"定价_费率\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1610614244674,\n" +
                "            \"label\": \"优惠券\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1610614253979,\n" +
                "            \"label\": \"用户\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1610614261073,\n" +
                "            \"label\": \"合同\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1610614278889,\n" +
                "            \"label\": \"支付_收银台\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1612347576065,\n" +
                "            \"label\": \"账单和还款\",\n" +
                "            \"children\": []\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1609746589297,\n" +
                "        \"label\": \"清结算\",\n" +
                "        \"children\": [\n" +
                "          {\n" +
                "            \"id\": 1611649629483,\n" +
                "            \"label\": \"放款流程\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1611649680660,\n" +
                "            \"label\": \"还款流程\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1611649686595,\n" +
                "            \"label\": \"赔付结清\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1612692121273,\n" +
                "            \"label\": \"二次结算\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1612773480006,\n" +
                "            \"label\": \"乐花卡还款\",\n" +
                "            \"children\": []\n" +
                "          },\n" +
                "          {\n" +
                "            \"id\": 1612773847060,\n" +
                "            \"label\": \"冲补流程\",\n" +
                "            \"children\": []\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1609746619488,\n" +
                "        \"label\": \"全流程用例\",\n" +
                "        \"children\": []\n" +
                "      }\n" +
                "    ],\n" +
                "    \"disable\": true,\n" +
                "    \"id\": 2,\n" +
                "    \"label\": \"业务线用例\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"children\": [\n" +
                "      {\n" +
                "        \"id\": 1571884944884,\n" +
                "        \"label\": \"双11大促优化\",\n" +
                "        \"children\": []\n" +
                "      }\n" +
                "    ],\n" +
                "    \"disable\": true,\n" +
                "    \"id\": 3,\n" +
                "    \"label\": \"个人用例\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"children\": [],\n" +
                "    \"disable\": true,\n" +
                "    \"id\": 4,\n" +
                "    \"label\": \"自动化用例\"\n" +
                "  }\n" +
                "]";
        JSONArray jsonArrayRoot = JSON.parseArray(jsonStr);
        for(int index =0;index<jsonArrayRoot.size();index++){
           JSONObject jsonTmp = jsonArrayRoot.getJSONObject(index);
           String label = jsonTmp.getString("label");
           if("业务线用例".equals(label)){
               JSONArray jsonArray = jsonTmp.getJSONArray("children");
               for(int indexModule =0;indexModule<jsonArray.size();indexModule++){
                   JSONObject jsonTmpModule = jsonArray.getJSONObject(indexModule);
                   String labelModule = jsonTmpModule.getString("label");
                   if(moduleList().contains(labelModule)){
                       JSONArray jsonArrayModule = jsonTmpModule.getJSONArray("children");
                           for(int indexSubModule =0;indexSubModule<jsonArrayModule.size();indexSubModule++) {
                               JSONObject jsonTmpSubModule = jsonArrayModule.getJSONObject(indexSubModule);
                               String labelSubModule = jsonTmpSubModule.getString("label");
                               BigInteger id = jsonTmpSubModule.getBigInteger("id");
                               System.out.println("id:"+id+"labelSubModule:"+labelSubModule);
                           }
                       }
                   }
               }
           }
        }



    private List<String> moduleList(){
        List<String>  moduleList = new ArrayList<>();
        moduleList.add("融资");
        moduleList.add("交易");
        moduleList.add("清结算");
        return moduleList;
    }
}
