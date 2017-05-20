package com.cronJob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

@Controller
@EnableAutoConfiguration
//@EntityScan(value = "com.cronJob")
@ComponentScan("com.cronJob")
@SpringBootApplication
public class CronJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(CronJobApplication.class, args);
    }

}
