package com.majian.springbootinit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.majian.springbootinit.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.majian")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.majian.springbootinit.serice")
public class MajianOjSystemUserSeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MajianOjSystemUserSeviceApplication.class, args);
    }

}
