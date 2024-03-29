package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJms
public class FileHandlingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileHandlingDemoApplication.class, args);
    }

}
