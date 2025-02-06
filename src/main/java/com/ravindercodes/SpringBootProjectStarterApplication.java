package com.ravindercodes;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@SpringBootApplication
@EnableEncryptableProperties
@EnableJpaAuditing
public class SpringBootProjectStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootProjectStarterApplication.class, args);
        log.info("Application started on port 8080: {}");
    }

}
