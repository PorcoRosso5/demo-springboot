package com.porco.javassist;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.porco.javassist")
@SpringBootApplication
public class DemoJavassistApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJavassistApplication.class, args);
    }

}
