package net.shopin.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import net.shopin.domain.User;
import net.shopin.domain.UserExample;
import net.shopin.mapper.UserMapper;
import net.shopin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Autowired(required = true)
    UserMapper userMapper;

    @Override
    public User selectUserById(Long Id) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(Id);
        List<User> userList = userMapper.selectByExample(example);
        return userList.get(0);
    }

    @Override
    public List<User> selectUserList() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdIsNotNull();
        List<User> userList = userMapper.selectByExample(example);
        return userList;
    }
}
