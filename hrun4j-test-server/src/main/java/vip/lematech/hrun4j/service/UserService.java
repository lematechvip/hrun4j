package vip.lematech.hrun4j.service;

import vip.lematech.hrun4j.core.entity.User;
import vip.lematech.hrun4j.core.entity.UserDto;

import java.util.List;


/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
public interface UserService {
    /**
     * 通过uid查询用户
     *
     * @param uid 用户id
     * @return 用户信息
     */
    UserDto findUserIndexByUid(int uid);

    /**
     * 通过uid查询用户
     *
     * @param uid user id
     * @return 用户信息
     */
    User findByUid(int uid);

    /**
     * 添加用户
     *
     * @param user user
     */
    void addUser(User user);

    /**
     * 更新用户信息
     *
     * @param user user
     */
    void updateUser(User user);

    /**
     * 删除用户
     *
     * @param uid user id
     * @return 删除标志
     */
    boolean deleteUser(int uid);

    /**
     * 查询用户列表
     *
     * @return 用户信息列表
     */
    List<User> lists();

    /**
     * 删库跑路
     */
    void deleteUsers();
}
