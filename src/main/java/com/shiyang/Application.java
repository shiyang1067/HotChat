package com.shiyang;

import com.shiyang.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @title: Application
 * @description:
 * @author: ShiYang
 * @date: 2019/07/16
 */
@SpringBootApplication
@MapperScan(basePackages = "com.shiyang.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(0,0);
    }

}
