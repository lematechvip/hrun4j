package vip.lematech.hrun4j.controller;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Maps;
import vip.lematech.hrun4j.core.annotation.ValidateRequest;
import vip.lematech.hrun4j.core.entity.User;
import vip.lematech.hrun4j.core.enums.CommonBusinessCode;
import vip.lematech.hrun4j.service.TokenService;
import vip.lematech.hrun4j.service.UserService;
import vip.lematech.hrun4j.vo.TokenVO;
import vip.lematech.hrun4j.vo.UserVO;
import vip.lematech.hrun4j.vo.base.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

/**
 * hrun4j 使用入门案例
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class Hrun4jController {
    @Autowired
    private TokenService tokenServiceImpl;

    @Autowired
    private UserService userServiceImpl;

    @GetMapping(value = "/")
    public R index() {
        return R.ok("Hello,Lematech~!");
    }

    @PostMapping(value = "/get-token")
    public R getToken(@RequestHeader(value = "device_sn") String deviceSN,
                      @RequestHeader(value = "os_platform", required = false) String osPlatform,
                      @RequestHeader(value = "app_version", required = false) String appVersion,
                      @RequestBody TokenVO tokenVO) {
        String expectSign = tokenServiceImpl.generateToken(deviceSN, osPlatform, appVersion);
        log.info("sign: {}", expectSign);
        boolean validateResult = tokenServiceImpl.validateToken(tokenVO.getSign(), expectSign);
        if (!validateResult) {
            return R.fail(CommonBusinessCode.Authorization_FAILED_EXCEPTION);
        }
        String token = RandomUtil.randomString(16);
        tokenServiceImpl.storyToken(deviceSN, token);
        Map resultData = Maps.newHashMap();
        resultData.put("token", token);
        return R.ok(resultData);
    }

    @PostMapping(value = "/user/{uid}")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R createUser(@PathVariable int uid, @RequestBody UserVO userVO) {
        User addUser = new User();
        addUser.setUid(uid);
        addUser.setName(userVO.getName());
        addUser.setPassword(userVO.getPassword());
        userServiceImpl.addUser(addUser);
        return R.ok("用户创建成功！");
    }

    @GetMapping(value = "/user/{uid}")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R queryUser(@PathVariable int uid) {
        User user = userServiceImpl.findByUid(uid);
        if (Objects.isNull(user)) {
            return R.fail(CommonBusinessCode.USER_IS_NOT_EXISTS_EXCEPTION);
        }
        return R.ok(user);
    }

    @PutMapping(value = "/user/{uid}")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R updateUser(@PathVariable int uid, @RequestBody UserVO userVO) {
        User addUser = new User();
        addUser.setUid(uid);
        addUser.setName(userVO.getName());
        addUser.setPassword(userVO.getPassword());
        userServiceImpl.updateUser(addUser);
        return R.ok("用户更新成功！");
    }

    @DeleteMapping(value = "/user/{uid}")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R deleteUser(@PathVariable int uid) {
        boolean delFlag = userServiceImpl.deleteUser(uid);
        if (!delFlag) {
            R.fail("删除用户失败！");
        }
        return R.ok("用户删除成功！");
    }

    @GetMapping(value = "/users/list")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R list() {
        return R.ok(userServiceImpl.lists());
    }

    @GetMapping(value = "/users/reset-all")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R resetAll() {
        userServiceImpl.deleteUsers();
        return R.ok();
    }

    @PostMapping(value = "/users/upload-image")
    @ValidateRequest(headerNames = {"device_sn", "token"})
    public R imageCheck(MultipartFile file1, MultipartFile file2) {
        log.info("文件名1：{},文件名2：{}", file1.getOriginalFilename(), file2.getOriginalFilename());
        return R.ok("文件上传成功！");
    }
}
