package vip.lematech.hrun4j.okhttp;

import com.alibaba.fastjson.JSONObject;

import org.testng.Assert;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
import vip.lematech.hrun4j.common.Constant;
import vip.lematech.hrun4j.helper.JsonHelper;
import vip.lematech.hrun4j.helper.LogHelper;

import java.util.ArrayList;

public class JsonCompareTest {
    @Test
    public void jsonCompareTest(){
        JSONObject actual = JSONObject.parseObject("{\"k\":\"a\",\"v\":\"b\"}");
        JSONObject expect = JSONObject.parseObject("{\"k\":\"a1\",\"v\":\"b1\"}");
        JsonHelper.BaseResult baseResult = JsonHelper.jsonValidateFactory(Constant.JSON_VALIDATE,actual,expect,"");
        Assert.assertEquals(baseResult.getWrongNumber(),2,"断言错误数量");
    }

    @Test
    public void jsonCompareIgnoreTest(){
        JSONObject actual = JSONObject.parseObject("{\"code\":\"010\",\"msg\":\"ok！\",\"data\":{\"total\":10,\"list\":[{\"id\":907,\"createTime\":\"2021-06-24 14:28:56\",\"modifyTime\":\"2021-06-24 14:28:56\",\"ruleId\":\"RC20210624022856236\",\"businessLineId\":8,\"ruleName\":\"【管仲】信托参数接口对接-优化需求\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"wetliu\",\"modifier\":\"wetliu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":533,\"createTime\":\"2020-11-12 19:45:30\",\"modifyTime\":\"2020-11-12 19:45:30\",\"ruleId\":\"RC20201112074530665\",\"businessLineId\":8,\"ruleName\":\"精准测试\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":167,\"createTime\":\"2020-02-27 20:42:24\",\"modifyTime\":\"2020-02-27 20:42:24\",\"ruleId\":\"RC20200227084224921\",\"businessLineId\":8,\"ruleName\":\"需求池\",\"ruleDesc\":\"需求池\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":35,\"createTime\":\"2019-11-04 17:30:02\",\"modifyTime\":\"2019-12-30 17:18:13\",\"ruleId\":\"RC20191104053002843\",\"businessLineId\":8,\"ruleName\":\"测试\",\"ruleDesc\":\"\",\"status\":0,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":110,\"createTime\":\"2019-12-27 16:02:02\",\"modifyTime\":\"2019-12-27 16:02:02\",\"ruleId\":\"RC20191227040202918\",\"businessLineId\":8,\"ruleName\":\"kris_test_rule\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"kriswu\",\"modifier\":\"kriswu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":99,\"createTime\":\"2019-12-17 15:51:10\",\"modifyTime\":\"2019-12-17 15:51:10\",\"ruleId\":\"RC20191217035110562\",\"businessLineId\":8,\"ruleName\":\"质量管理中心\",\"ruleDesc\":\"质量管理中心\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":61,\"createTime\":\"2019-11-19 14:42:43\",\"modifyTime\":\"2019-11-25 13:53:31\",\"ruleId\":\"RC20191119024243882\",\"businessLineId\":8,\"ruleName\":\"自动化建设\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":62,\"createTime\":\"2019-11-19 14:42:58\",\"modifyTime\":\"2019-11-19 17:51:28\",\"ruleId\":\"RC20191119024258928\",\"businessLineId\":8,\"ruleName\":\"安全测试\",\"ruleDesc\":\"安全测试\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":38,\"createTime\":\"2019-11-05 10:46:41\",\"modifyTime\":\"2019-11-05 10:46:41\",\"ruleId\":\"RC20191105104641446\",\"businessLineId\":8,\"ruleName\":\"进度管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":37,\"createTime\":\"2019-11-05 10:46:07\",\"modifyTime\":\"2019-11-05 10:46:07\",\"ruleId\":\"RC20191105104607369\",\"businessLineId\":8,\"ruleName\":\"需求管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0}],\"pageNum\":1,\"pageSize\":10,\"size\":10,\"startRow\":1,\"endRow\":10,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1}}");
        JSONObject expect = JSONObject.parseObject("{\"code\":\"00\",\"msg\":\"ok1！\",\"data\":{\"total\":10,\"list\":[{\"id\":907,\"createTime\":\"2021-06-24 14:28:56\",\"modifyTime\":\"2021-06-24 14:28:56\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"wetliu\",\"modifier\":\"wetliu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":533,\"createTime\":\"2020-11-12 19:45:30\",\"modifyTime\":\"2020-11-12 19:45:30\",\"ruleId\":\"RC20201112074530665\",\"businessLineId\":8,\"ruleName\":\"精准测试\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":167,\"createTime\":\"2020-02-27 20:42:24\",\"modifyTime\":\"2020-02-27 20:42:24\",\"ruleId\":\"RC20200227084224921\",\"businessLineId\":8,\"ruleName\":\"需求池\",\"ruleDesc\":\"需求池\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":35,\"createTime\":\"2019-11-04 17:30:02\",\"modifyTime\":\"2019-12-30 17:18:13\",\"ruleId\":\"RC20191104053002843\",\"businessLineId\":8,\"ruleName\":\"测试\",\"ruleDesc\":\"\",\"status\":0,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":110,\"createTime\":\"2019-12-27 16:02:02\",\"modifyTime\":\"2019-12-27 16:02:02\",\"ruleId\":\"RC20191227040202918\",\"businessLineId\":8,\"ruleName\":\"kris_test_rule\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"kriswu\",\"modifier\":\"kriswu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":99,\"createTime\":\"2019-12-17 15:51:10\",\"modifyTime\":\"2019-12-17 15:51:10\",\"ruleId\":\"RC20191217035110562\",\"businessLineId\":8,\"ruleName\":\"质量管理中心\",\"ruleDesc\":\"质量管理中心\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":61,\"createTime\":\"2019-11-19 14:42:43\",\"modifyTime\":\"2019-11-25 13:53:31\",\"ruleId\":\"RC20191119024243882\",\"businessLineId\":8,\"ruleName\":\"自动化建设\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":62,\"createTime\":\"2019-11-19 14:42:58\",\"modifyTime\":\"2019-11-19 17:51:28\",\"ruleId\":\"RC20191119024258928\",\"businessLineId\":8,\"ruleName\":\"安全测试\",\"ruleDesc\":\"安全测试\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":38,\"createTime\":\"2019-11-05 10:46:41\",\"modifyTime\":\"2019-11-05 10:46:41\",\"ruleId\":\"RC20191105104641446\",\"businessLineId\":8,\"ruleName\":\"进度管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":37,\"createTime\":\"2019-11-05 10:46:07\",\"modifyTime\":\"2019-11-05 10:46:07\",\"ruleId\":\"RC20191105104607369\",\"businessLineId\":8,\"ruleName\":\"需求管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0}],\"pageNum\":1,\"pageSize\":10,\"size\":10,\"startRow\":1,\"endRow\":10,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1}}");
        JsonHelper.BaseResult baseResult = JsonHelper.jsonValidateFactory(Constant.JSON_VALIDATE,actual,expect,"msg");
        LogHelper.info("错误数量：{}",baseResult.getWrongNumber());
        ArrayList<JsonHelper.ResultDetail> resultDetails = baseResult.getResultDetail();
        for(JsonHelper.ResultDetail resultDetail : resultDetails){
            LogHelper.info("On the $.v node: :{},actual value:{},expect value:{},msg:{}",resultDetail.getPrefix(),resultDetail.getActual(),resultDetail.getExpect(),resultDetail.getMsg());
        }
        Assert.assertEquals(baseResult.getWrongNumber(),1,"断言错误数量");
    }

    @Test
    @ExpectedExceptions(ClassCastException.class)
    public void dataTypeNotEqualCompareIgnoreTest(){
        JSONObject actual = JSONObject.parseObject("{\"code\":\"010\",\"msg\":\"ok！\",\"data\":{\"total\":10,\"list\":[{\"id\":907,\"createTime\":\"2021-06-24 14:28:56\",\"modifyTime\":\"2021-06-24 14:28:56\",\"ruleId\":\"RC20210624022856236\",\"businessLineId\":8,\"ruleName\":\"【管仲】信托参数接口对接-优化需求\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"wetliu\",\"modifier\":\"wetliu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":533,\"createTime\":\"2020-11-12 19:45:30\",\"modifyTime\":\"2020-11-12 19:45:30\",\"ruleId\":\"RC20201112074530665\",\"businessLineId\":8,\"ruleName\":\"精准测试\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":167,\"createTime\":\"2020-02-27 20:42:24\",\"modifyTime\":\"2020-02-27 20:42:24\",\"ruleId\":\"RC20200227084224921\",\"businessLineId\":8,\"ruleName\":\"需求池\",\"ruleDesc\":\"需求池\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":35,\"createTime\":\"2019-11-04 17:30:02\",\"modifyTime\":\"2019-12-30 17:18:13\",\"ruleId\":\"RC20191104053002843\",\"businessLineId\":8,\"ruleName\":\"测试\",\"ruleDesc\":\"\",\"status\":0,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":110,\"createTime\":\"2019-12-27 16:02:02\",\"modifyTime\":\"2019-12-27 16:02:02\",\"ruleId\":\"RC20191227040202918\",\"businessLineId\":8,\"ruleName\":\"kris_test_rule\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"kriswu\",\"modifier\":\"kriswu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":99,\"createTime\":\"2019-12-17 15:51:10\",\"modifyTime\":\"2019-12-17 15:51:10\",\"ruleId\":\"RC20191217035110562\",\"businessLineId\":8,\"ruleName\":\"质量管理中心\",\"ruleDesc\":\"质量管理中心\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":61,\"createTime\":\"2019-11-19 14:42:43\",\"modifyTime\":\"2019-11-25 13:53:31\",\"ruleId\":\"RC20191119024243882\",\"businessLineId\":8,\"ruleName\":\"自动化建设\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":62,\"createTime\":\"2019-11-19 14:42:58\",\"modifyTime\":\"2019-11-19 17:51:28\",\"ruleId\":\"RC20191119024258928\",\"businessLineId\":8,\"ruleName\":\"安全测试\",\"ruleDesc\":\"安全测试\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":38,\"createTime\":\"2019-11-05 10:46:41\",\"modifyTime\":\"2019-11-05 10:46:41\",\"ruleId\":\"RC20191105104641446\",\"businessLineId\":8,\"ruleName\":\"进度管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":37,\"createTime\":\"2019-11-05 10:46:07\",\"modifyTime\":\"2019-11-05 10:46:07\",\"ruleId\":\"RC20191105104607369\",\"businessLineId\":8,\"ruleName\":\"需求管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0}],\"pageNum\":1,\"pageSize\":10,\"size\":10,\"startRow\":1,\"endRow\":10,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1}}");
        //JSONObject expect = JSONObject.parseObject("xxx");
        JsonHelper.BaseResult baseResult = JsonHelper.jsonValidateFactory(Constant.JSON_VALIDATE,actual,"expect","msg");
        LogHelper.info("错误数量：{}",baseResult.getWrongNumber());
        ArrayList<JsonHelper.ResultDetail> resultDetails = baseResult.getResultDetail();
        for(JsonHelper.ResultDetail resultDetail : resultDetails){
            LogHelper.info("prefix:{},actual:{},expect:{},msg:{}",resultDetail.getPrefix(),resultDetail.getActual(),resultDetail.getExpect(),resultDetail.getMsg());
        }
        Assert.assertEquals(baseResult.getWrongNumber(),1,"断言错误数量");
    }

    @Test
    public void jsonCompareNodeNotExistTest(){
        JSONObject actual = JSONObject.parseObject("{\"code\":\"010\",\"msg\":\"ok！\",\"data\":{\"list\":[{\"id\":907,\"createTime\":\"2021-06-24 14:28:56\",\"modifyTime\":\"2021-06-24 14:28:56\",\"ruleId\":\"RC20210624022856236\",\"businessLineId\":8,\"ruleName\":\"【管仲】信托参数接口对接-优化需求\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"wetliu\",\"modifier\":\"wetliu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":533,\"createTime\":\"2020-11-12 19:45:30\",\"modifyTime\":\"2020-11-12 19:45:30\",\"ruleId\":\"RC20201112074530665\",\"businessLineId\":8,\"ruleName\":\"精准测试\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":167,\"createTime\":\"2020-02-27 20:42:24\",\"modifyTime\":\"2020-02-27 20:42:24\",\"ruleId\":\"RC20200227084224921\",\"businessLineId\":8,\"ruleName\":\"需求池\",\"ruleDesc\":\"需求池\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":35,\"createTime\":\"2019-11-04 17:30:02\",\"modifyTime\":\"2019-12-30 17:18:13\",\"ruleId\":\"RC20191104053002843\",\"businessLineId\":8,\"ruleName\":\"测试\",\"ruleDesc\":\"\",\"status\":0,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":110,\"createTime\":\"2019-12-27 16:02:02\",\"modifyTime\":\"2019-12-27 16:02:02\",\"ruleId\":\"RC20191227040202918\",\"businessLineId\":8,\"ruleName\":\"kris_test_rule\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"kriswu\",\"modifier\":\"kriswu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":99,\"createTime\":\"2019-12-17 15:51:10\",\"modifyTime\":\"2019-12-17 15:51:10\",\"ruleId\":\"RC20191217035110562\",\"businessLineId\":8,\"ruleName\":\"质量管理中心\",\"ruleDesc\":\"质量管理中心\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":61,\"createTime\":\"2019-11-19 14:42:43\",\"modifyTime\":\"2019-11-25 13:53:31\",\"ruleId\":\"RC20191119024243882\",\"businessLineId\":8,\"ruleName\":\"自动化建设\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":62,\"createTime\":\"2019-11-19 14:42:58\",\"modifyTime\":\"2019-11-19 17:51:28\",\"ruleId\":\"RC20191119024258928\",\"businessLineId\":8,\"ruleName\":\"安全测试\",\"ruleDesc\":\"安全测试\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":38,\"createTime\":\"2019-11-05 10:46:41\",\"modifyTime\":\"2019-11-05 10:46:41\",\"ruleId\":\"RC20191105104641446\",\"businessLineId\":8,\"ruleName\":\"进度管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":37,\"createTime\":\"2019-11-05 10:46:07\",\"modifyTime\":\"2019-11-05 10:46:07\",\"ruleId\":\"RC20191105104607369\",\"businessLineId\":8,\"ruleName\":\"需求管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0}],\"pageNum\":1,\"pageSize\":10,\"size\":10,\"startRow\":1,\"endRow\":10,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1}}");
        JSONObject expect = JSONObject.parseObject("{\"code\":\"00\",\"msg\":\"ok1！\",\"data\":{\"total\":10,\"list\":[{\"id\":907,\"createTime\":\"2021-06-24 14:28:56\",\"modifyTime\":\"2021-06-24 14:28:56\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"wetliu\",\"modifier\":\"wetliu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":533,\"createTime\":\"2020-11-12 19:45:30\",\"modifyTime\":\"2020-11-12 19:45:30\",\"ruleId\":\"RC20201112074530665\",\"businessLineId\":8,\"ruleName\":\"精准测试\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":167,\"createTime\":\"2020-02-27 20:42:24\",\"modifyTime\":\"2020-02-27 20:42:24\",\"ruleId\":\"RC20200227084224921\",\"businessLineId\":8,\"ruleName\":\"需求池\",\"ruleDesc\":\"需求池\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":35,\"createTime\":\"2019-11-04 17:30:02\",\"modifyTime\":\"2019-12-30 17:18:13\",\"ruleId\":\"RC20191104053002843\",\"businessLineId\":8,\"ruleName\":\"测试\",\"ruleDesc\":\"\",\"status\":0,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":110,\"createTime\":\"2019-12-27 16:02:02\",\"modifyTime\":\"2019-12-27 16:02:02\",\"ruleId\":\"RC20191227040202918\",\"businessLineId\":8,\"ruleName\":\"kris_test_rule\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"kriswu\",\"modifier\":\"kriswu\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":99,\"createTime\":\"2019-12-17 15:51:10\",\"modifyTime\":\"2019-12-17 15:51:10\",\"ruleId\":\"RC20191217035110562\",\"businessLineId\":8,\"ruleName\":\"质量管理中心\",\"ruleDesc\":\"质量管理中心\",\"status\":1,\"creator\":\"arkhe\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":61,\"createTime\":\"2019-11-19 14:42:43\",\"modifyTime\":\"2019-11-25 13:53:31\",\"ruleId\":\"RC20191119024243882\",\"businessLineId\":8,\"ruleName\":\"自动化建设\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":62,\"createTime\":\"2019-11-19 14:42:58\",\"modifyTime\":\"2019-11-19 17:51:28\",\"ruleId\":\"RC20191119024258928\",\"businessLineId\":8,\"ruleName\":\"安全测试\",\"ruleDesc\":\"安全测试\",\"status\":1,\"creator\":\"system\",\"modifier\":\"arkhe\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":38,\"createTime\":\"2019-11-05 10:46:41\",\"modifyTime\":\"2019-11-05 10:46:41\",\"ruleId\":\"RC20191105104641446\",\"businessLineId\":8,\"ruleName\":\"进度管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0},{\"id\":37,\"createTime\":\"2019-11-05 10:46:07\",\"modifyTime\":\"2019-11-05 10:46:07\",\"ruleId\":\"RC20191105104607369\",\"businessLineId\":8,\"ruleName\":\"需求管理\",\"ruleDesc\":\"\",\"status\":1,\"creator\":\"jimliang\",\"modifier\":\"jimliang\",\"isVersionRule\":0,\"isOrAndAnd\":0}],\"pageNum\":1,\"pageSize\":10,\"size\":10,\"startRow\":1,\"endRow\":10,\"pages\":1,\"prePage\":0,\"nextPage\":0,\"isFirstPage\":true,\"isLastPage\":true,\"hasPreviousPage\":false,\"hasNextPage\":false,\"navigatePages\":8,\"navigatepageNums\":[1],\"navigateFirstPage\":1,\"navigateLastPage\":1,\"firstPage\":1,\"lastPage\":1}}");
        JsonHelper.BaseResult baseResult = JsonHelper.jsonValidateFactory(Constant.JSON_VALIDATE,actual,expect,"msg");
        LogHelper.info("错误数量：{}",baseResult.getWrongNumber());
        ArrayList<JsonHelper.ResultDetail> resultDetails = baseResult.getResultDetail();
        for(JsonHelper.ResultDetail resultDetail : resultDetails){
            LogHelper.info("prefix:{},actual:{},expect:{},msg:{}",resultDetail.getPrefix(),resultDetail.getActual(),resultDetail.getExpect(),resultDetail.getMsg());
        }
        Assert.assertEquals(baseResult.getWrongNumber(),2,"断言错误数量");
    }

    @Test
    public void jsonSchemaValidateTest(){
        String schema = "{\n" +
                "  \"$schema\": \"http://json-schema.org/draft-07/schema\",\n" +
                "  \"description\": \"hrun4j testsuite schema v2 definition\",\n" +
                "  \"type\": \"object\",\n" +
                "  \"definitions\": {\n" +
                "    \"testcase\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"name\": {\n" +
                "          \"description\": \"testcase reference, value is testcase file relative path\",\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"variables\": {\n" +
                "          \"description\": \"testcase reference, value is testcase file relative path\",\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"parameters\": {\n" +
                "          \"description\": \"generate cartesian product variables with parameters, each group of variables will be run once\",\n" +
                "          \"type\": \"object\"\n" +
                "        },\n" +
                "        \"testcase\": {\n" +
                "          \"description\": \"testcase reference, value is testcase file relative path\",\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"required\": [\n" +
                "        \"testcase\"\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"properties\": {\n" +
                "    \"config\": {\n" +
                "      \"$ref\": \"resource:/schemas/common.json#/definitions/config\"\n" +
                "    },\n" +
                "    \"testcases\": {\n" +
                "      \"description\": \"testcase of a testsuite\",\n" +
                "      \"type\": \"array\",\n" +
                "      \"minItems\": 1,\n" +
                "      \"items\": {\n" +
                "        \"$ref\": \"resource:/schemas/testsuite.json#/definitions/testcase\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"required\": [\n" +
                "    \"config\",\n" +
                "    \"testcases\"\n" +
                "  ],\n" +
                "  \"examples\": [\n" +
                "    {\n" +
                "      \"config\": {\n" +
                "        \"name\": \"testsuite name\"\n" +
                "      },\n" +
                "      \"testcases\": [\n" +
                "        {\n" +
                "          \"name\": \"testcase 1\",\n" +
                "          \"testcase\": \"/path/to/testcase1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"testcase 2\",\n" +
                "          \"testcase\": \"/path/to/testcase2\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"config\": {\n" +
                "        \"name\": \"demo testsuite\",\n" +
                "        \"variables\": {\n" +
                "          \"device_sn\": \"XYZ\"\n" +
                "        },\n" +
                "        \"base_url\": \"http://127.0.0.1:5000\"\n" +
                "      },\n" +
                "      \"testcases\": [\n" +
                "        {\n" +
                "          \"name\": \"call demo_testcase with data 1\",\n" +
                "          \"testcase\": \"path/to/demo_testcase.yml\",\n" +
                "          \"variables\": {\n" +
                "            \"device_sn\": \"${device_sn}\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"call demo_testcase with data 2\",\n" +
                "          \"testcase\": \"path/to/demo_testcase.yml\",\n" +
                "          \"variables\": {\n" +
                "            \"device_sn\": \"${device_sn}\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
        String data = "{\n" +
                "  \"config\": {\n" +
                "    \"name\": \"测试用例集配置\",\n" +
                "    \"base_url\": \"https://api.apiopen.top\"\n" +
                "  },\n" +
                "  \"testcases\": [\n" +
                "    {\n" +
                "      \"name\": \"Postman Echo GET Request\",\n" +
                "      \"testcase\": \"testcases/postman/get/getScene.yml\",\n" +
                "      \"parameters\": {\n" +
                "        \"page\": [\n" +
                "          1,\n" +
                "          2,\n" +
                "          3,\n" +
                "          4,\n" +
                "          5\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Postman Echo postFormData And postRawText Request\",\n" +
                "      \"testcase\": \"testcases/postman/post/postScene.yml\"" +
                "\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        JSONObject schemaObj = JSONObject.parseObject(schema);
        JSONObject dataObj = JSONObject.parseObject(data);
        JsonHelper.jsonValidateFactory(Constant.JSON_SCHEMA_VALIDATE,schemaObj, dataObj,null);
    }
}
