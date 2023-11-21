package com.example.batchinsertspringbootspringbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchApplication {
    public static void main(String[] args) {
        var apringApp = new SpringApplication(SpringBatchApplication.class);
        apringApp.setAdditionalProfiles("spring-boot");
        apringApp.run(args);
    }
}
