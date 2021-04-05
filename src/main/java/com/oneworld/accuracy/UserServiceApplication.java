package com.oneworld.accuracy;

import de.codecentric.boot.admin.server.config.AdminServerHazelcastAutoConfiguration;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude =AdminServerHazelcastAutoConfiguration.class)
@EnableJpaAuditing
@Configuration
@EnableAdminServer
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
