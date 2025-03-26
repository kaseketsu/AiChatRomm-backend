package com.itflower.flowerhaiguisoup;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itflower.flowerhaiguisoup.mapper")
public class FlowerhaiguisoupApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowerhaiguisoupApplication.class, args);
    }

}
