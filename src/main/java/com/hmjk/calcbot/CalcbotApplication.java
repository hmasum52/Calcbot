package com.hmjk.calcbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CalcbotApplication {
    private static final Logger log = LoggerFactory.getLogger(CalcbotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CalcbotApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(Environment environment) {
        return args -> {
            log.info("message from application.properties " + environment.getProperty("message-from-application-properties"));
            log.info("hi: "+environment.getProperty("hi"));
        };
    }

}
