package vip.lematech.hrun4j.testcases.scene;

import vip.lematech.hrun4j.Hrun4j;
import vip.lematech.hrun4j.core.engine.TestCaseExecutorEngine;
import vip.lematech.hrun4j.entity.testcase.TestCase;
import org.testng.annotations.Test;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 */
public class SceneTest extends Hrun4j {

    /**
     * 重置系统
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void loginAndResetAll(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 登录并添加用户信息
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void loginAndAddUser(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 查询新增用户信息
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void queryAssignUserInfo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 删除新增用户
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void delAssignUserInfo(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


    /**
     * 登录并添更新用户信息
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void loginAndUpdateUser(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }

    /**
     * 上用户头像
     *
     * @param testCase
     */
    @Test(dataProvider = "dataProvider")
    public void loginAndUploadUser(TestCase testCase) {
        TestCaseExecutorEngine.getInstance().execute(testCase);
    }


}
