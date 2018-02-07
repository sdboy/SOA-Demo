package net.shopin;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangyong@shopin.cn
 * @date 2018/2/5 11:01
 */
@SpringBootApplication
@DubboComponentScan("net.shopin.service.impl")
@MapperScan(basePackages = "net.shopin.mapper")
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }
}
