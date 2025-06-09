package com.thesimpleside.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TheSimpleSideNewsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TheSimpleSideNewsApplication.class, args);
    }
} 