package net.shopin.service;

import net.shopin.domain.User;

import java.util.List;

/**
 * @author littledream1502@gmail.com
 * @date 2017/12/21
 * @desc 用户操作接口
 */
public interface UserService {

    /**
     * 根据主键查询用户信息
     * @param Id
     * @return
     */
    public User selectUserById(Long Id);

    public List<User> selectUserList();
}
