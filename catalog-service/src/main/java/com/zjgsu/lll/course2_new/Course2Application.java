package com.zjgsu.lll.course2_new;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Course2Application {

    public static void main(String[] args) {
        SpringApplication.run(Course2Application.class, args);
    }

}