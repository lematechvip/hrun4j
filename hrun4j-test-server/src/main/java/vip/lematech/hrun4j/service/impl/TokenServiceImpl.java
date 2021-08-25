package vip.lematech.hrun4j.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.core.constant.CommonConstant;
import vip.lematech.hrun4j.core.exception.PlatformException;
import vip.lematech.hrun4j.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
@Service("TokenServiceImpl")
@Slf4j
public class TokenServiceImpl implements TokenService {
    private static Map<String, String> tokenMap = Maps.newHashMap();

    @Override
    public String generateToken(String deviceSN, String osPlatform, String appVersion) {
        String sign;
        try {
            StringBuffer content = new StringBuffer();
            content.append(deviceSN).append(osPlatform).append(appVersion);
            String crypContent = content.toString();
            sign = SecureUtil.hmac(HmacAlgorithm.HmacSHA1, CommonConstant.TOKEN_KEY).digestHex(crypContent);
            log.info("加密秘钥：{},加密内容：{},生成的签名：{}", CommonConstant.TOKEN_KEY, crypContent, sign);
        } catch (Exception e) {
            String exceptionMsg = String.format("加签异常，异常信息：%s", e.getMessage());
            throw new PlatformException(exceptionMsg);
        }
        return sign;
    }

    @Override
    public boolean validateToken(String actaulToken, String expectToken) {
        if (StrUtil.isEmpty(actaulToken)) {
            return false;
        }
        if (StrUtil.isEmpty(expectToken)) {
            return false;
        }
        return expectToken.equals(actaulToken);
    }

    @Override
    public void storyToken(String deviceSN, String token) {
        tokenMap.put(deviceSN, token);
    }

    @Override
    public String queryToken(String deviceSN) {
        return tokenMap.get(deviceSN);
    }
}
