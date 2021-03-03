package io.lematech.httprunner4j.service;


/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className TokenService
 * @description TODO
 * @created 2021/3/2 8:08 下午
 * @publicWechat lematech
 */
public interface TokenService {
    /**
     * 生成签名
     * @param deviceSN
     * @param osPlatform
     * @param appVersion
     * @return
     */
    String generateToken(String deviceSN,String osPlatform,String appVersion);

    /**
     * 验签
     * @param actaulToken
     * @param expectToken
     * @return
     */
    boolean validateToken(String actaulToken,String expectToken);

    /**
     * 建立token和devicesn映射关系并存储
     * @param deviceSN
     * @param token
     */
    void storyToken(String deviceSN,String token);

    /**
     * 根据devicesn提取token
     * @param deviceSN
     * @return
     */
    String queryToken(String deviceSN);

}
