package com.hmall.pay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.hmall.pay.mapper")
@SpringBootApplication
//@ComponentScan(basePackages = {"com.hmall.pay", "com.hmall.api"})//只能扫描本模块以及引入的依赖中已存在的包
@EnableFeignClients(basePackages = "com.hmall.api.client")//用于扫描FeignClient，使用远程调用
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }
}