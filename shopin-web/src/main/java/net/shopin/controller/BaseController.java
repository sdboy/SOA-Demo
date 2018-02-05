package net.shopin.controller;

import net.shopin.bean.ResultBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyong@shopin.cn
 * @date 2018/2/5 12:28
 */
@RestController
public class BaseController {

    @RequestMapping("/index")
    @ResponseBody
    public ResultBean<String> index() {
        return new ResultBean<String>("Hello,SHOPIN-SOA");
    }
}
