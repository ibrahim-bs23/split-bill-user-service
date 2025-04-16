package com.brainstation23.skeleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SkeletonServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkeletonServiceApplication.class, args);
    }

}
