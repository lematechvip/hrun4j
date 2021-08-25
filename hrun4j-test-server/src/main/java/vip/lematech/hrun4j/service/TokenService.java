package vip.lematech.hrun4j.service;


/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
public interface TokenService {
    /**
     * 生成签名
     *
     * @param deviceSN deviceSN
     * @param osPlatform osPlatform
     * @param appVersion appVersion
     * @return token value
     */
    String generateToken(String deviceSN, String osPlatform, String appVersion);

    /**
     * 验签
     *
     * @param actualToken actualToken
     * @param expectToken expectToken
     * @return 验证token通过或不通过
     */
    boolean validateToken(String actualToken, String expectToken);

    /**
     * 建立token和devicesn映射关系并存储
     *
     * @param deviceSN deviceSN
     * @param token token
     */
    void storyToken(String deviceSN, String token);

    /**
     * 根据devicesn提取token
     *
     * @param deviceSN deviceSN
     * @return token
     */
    String queryToken(String deviceSN);

}
