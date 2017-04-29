package com.sgbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

@Controller
@EnableAutoConfiguration
@EntityScan(value = "com.sgbd.model")
@ComponentScan("com.sgbd")
@SpringBootApplication
public class SgbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgbdApplication.class, args);
    }
}
