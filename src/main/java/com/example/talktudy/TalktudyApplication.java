package com.example.talktudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TalktudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalktudyApplication.class, args);
    }

}
