package com.xxxx.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//包扫描的路径
@MapperScan("com.xxxx.seckill.mapper")
public class SeckillDemoApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SeckillDemoApplication.class, args);

    }

}
