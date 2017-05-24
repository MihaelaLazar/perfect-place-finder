package com.sgbd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;


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

//    @Bean
//    public Docket newsApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("greetings")
//                .apiInfo(apiInfo())
//                .select()
//                .paths(regex("/greeting.*"))
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Spring REST Sample with Swagger")
//                .description("Spring REST Sample with Swagger")
//                .termsOfServiceUrl("http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open")
//                .contact("Niklas Heidloff")
//                .license("Apache License Version 2.0")
//                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
//                .version("2.0")
//                .build();
//    }
}
