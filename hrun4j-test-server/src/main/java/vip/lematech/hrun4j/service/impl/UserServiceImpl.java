package vip.lematech.hrun4j.service.impl;


import vip.lematech.hrun4j.core.entity.User;
import vip.lematech.hrun4j.core.entity.UserDto;
import vip.lematech.hrun4j.core.exception.PlatformException;
import vip.lematech.hrun4j.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lematech@foxmail.com
 * @version 1.0.1
 * website https://www.lematech.vip/
 */
@Service
public class UserServiceImpl implements UserService {
    private static List<User> users = new ArrayList();

    @Override
    public UserDto findUserIndexByUid(int uid) {
        for (int index = 0; index < users.size(); index++) {
            User user = users.get(index);
            if (user.getUid() == uid) {
                UserDto userDto = new UserDto();
                userDto.setIndex(index);
                userDto.setUser(user);
                return userDto;
            }
        }
        return null;
    }

    @Override
    public User findByUid(int uid) {
        for (User user : users) {
            if (user.getUid() == uid) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void addUser(User user) {
        int uid = user.getUid();
        User queryUser = findByUid(uid);
        if (Objects.isNull(queryUser)) {
            users.add(user);
            return;
        }
        String exceptionMsg = String.format("用户：%s 已存在", uid);
        throw new PlatformException(exceptionMsg);
    }

    @Override
    public void updateUser(User user) {
        int uid = user.getUid();
        UserDto queryUser = findUserIndexByUid(uid);
        if (Objects.isNull(queryUser)) {
            String exceptionMsg = String.format("用户：%s 不存在", uid);
            throw new PlatformException(exceptionMsg);
        }
        queryUser.getUser().setName(user.getName());
        queryUser.getUser().setPassword(user.getPassword());
        users.set(queryUser.getIndex(), queryUser.getUser());
    }

    @Override
    public boolean deleteUser(int uid) {
        User user = this.findByUid(uid);
        return users.remove(user);
    }

    @Override
    public List<User> lists() {
        return users;
    }

    @Override
    public void deleteUsers() {
        users.clear();
    }
}
