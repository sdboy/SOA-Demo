package net.shopin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import net.shopin.bean.ResultBean;
import net.shopin.domain.User;
import net.shopin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangyong@shopin.cn
 * @date 2018/2/5 12:50
 */
@Controller
public class UserController {

    @Reference(version = "1.0.0")
    UserService userService;

    @RequestMapping("/selectUserById/{userId}")
    @ResponseBody
    public ResultBean<User> selectUserById(@PathVariable Long userId) {
        return new ResultBean<User>(userService.selectUserById(userId));
    }
}
