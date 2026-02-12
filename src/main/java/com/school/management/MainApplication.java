package com.school.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class MainApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        System.out.println("====================================");
        System.out.println("School Management System Started!");
        System.out.println("Access at: http://localhost:8080");
        System.out.println("Default credentials: principal / sam123");
        System.out.println("====================================");
    }
}