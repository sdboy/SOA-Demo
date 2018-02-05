package net.shopin;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangyong@shopin.cn
 * @date 2018/2/5 12:53
 */
@SpringBootApplication
@DubboComponentScan(basePackages = {"net.shopin.controller"})
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
}
