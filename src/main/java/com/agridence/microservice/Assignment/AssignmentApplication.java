package com.agridence.microservice.Assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.agridence.microservice.Assignment.model"})
@EnableJpaRepositories(basePackages = {"com.agridence.microservice.Assignment.repository"})
public class AssignmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }
}
