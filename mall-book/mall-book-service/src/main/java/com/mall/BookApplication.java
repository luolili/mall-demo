package com.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.mall.book.mapper")
@EnableCaching
@EnableAsync
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.mall.book.BookApplication.class, args);
    }


}

