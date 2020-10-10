package com.feiqu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class FeiquAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeiquAdminApplication.class, args);
    }
}
