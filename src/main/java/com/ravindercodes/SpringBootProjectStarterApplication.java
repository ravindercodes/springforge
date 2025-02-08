package com.ravindercodes;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaAuditing
public class SpringBootProjectStarterApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringBootProjectStarterApplication.class);
        Environment env = app.run(args).getEnvironment();

        String port = env.getProperty("server.port", "8080");
        String applicationName = env.getProperty("server.name", "SpringForge");

        log.info("🚀 {} started on port: {}", applicationName, port);
        System.out.printf("\u001B[32m 🚀 %s started on port: %s \u001B[0m%n", applicationName, port);
    }

}