package com.sgbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


//@Controller
@EnableAutoConfiguration
@EntityScan(value = "com.sgbd.model")
@ComponentScan("com.sgbd")
@EnableSwagger2
@SpringBootApplication
public class SgbdApplication {

    public static void main(String[] args) {
        SpringApplication.run(SgbdApplication.class, args);
    }
}
