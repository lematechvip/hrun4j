package io.lemetech.httprunner.test;

import cn.hutool.core.util.StrUtil;
import com.sangupta.jerry.util.UriUtils;
import com.sun.deploy.util.URLUtil;
import com.sun.jndi.toolkit.url.UrlUtil;
import io.lematech.httprunner4j.widget.log.MyLog;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TestUtil
 * @description TODO
 * @created 2021/5/8 5:30 下午
 * @publicWechat lematech
 */
public class TestUtil {
    @Test
    public void testGetPath() {
        String urlStr = "https://databank.oa.fenqile.com/json_diff?k=v&xx=11";
        try {
            URL url = new URL(urlStr);
            MyLog.info(UriUtils.extractPath(urlStr));
            MyLog.info(UriUtils.getBaseUrl(urlStr));
            MyLog.info(url.getQuery());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
