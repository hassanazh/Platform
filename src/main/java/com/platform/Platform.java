package com.platform;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Platform {
    private final static Logger logger = Logger.getLogger(Platform.class);
    public static void main(String[] args) {
        logger.info("Starting platform application.");
        SpringApplication.run(Platform.class, args);
    }
}
