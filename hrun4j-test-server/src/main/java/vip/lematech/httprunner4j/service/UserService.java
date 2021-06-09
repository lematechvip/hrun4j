package vip.lematech.httprunner4j.service;

import vip.lematech.httprunner4j.core.UserDto;
import vip.lematech.httprunner4j.core.entity.User;

import java.util.List;

/**
 * @author lematech@foxmail.com
 * @version 1.0.0
 * @className UserService
 * @description TODO
 * @created 2021/3/3 2:07 下午
 * @publicWechat lematech
 */
public interface UserService {
    /**
     * 通过uid查询用户
     *
     * @param uid
     * @return
     */
    UserDto findUserIndexByUid(int uid);

    /**
     * 通过uid查询用户
     *
     * @param uid
     * @return
     */
    User findByUid(int uid);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    void addUser(User user);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 删除用户
     *
     * @param uid
     * @return
     */
    boolean deleteUser(int uid);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<User> lists();

    /**
     * 删库跑路
     */
    void deleteUsers();
}
