package io.lematech.httprunner4j;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.lematech.httprunner4j.common.Constant;
import io.lematech.httprunner4j.config.RunnerConfig;
import io.lematech.httprunner4j.utils.RegularUtil;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestSmartDoc
 * @description TODO
 * @created 2021/3/16 10:57 上午
 * @publicWechat lematech
 */
@Slf4j
public class TestSmartDoc {
    @Test
    public void testBuilderControllersApiSimple() {
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("k1", "v1");
        list.add(map);
        List subList = new ArrayList();
        Map subMap = new HashMap();
        subMap.put("k1", "v2");
        subList.add(subMap);
    }


}
