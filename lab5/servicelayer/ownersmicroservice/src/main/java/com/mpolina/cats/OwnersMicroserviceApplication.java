package com.mpolina.cats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.mpolina.cats")
@EntityScan("com.mpolina.cats.entity")
@EnableJpaRepositories("com.mpolina.cats.repository")
@ComponentScan("com.mpolina.cats.*")
public class OwnersMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OwnersMicroserviceApplication.class, args);
    }

}