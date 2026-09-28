package com.example.MaintainIt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MaintainitApplication {

    public static void main(String[] args) {

        SpringApplication.run(MaintainitApplication.class, args);

    }
}