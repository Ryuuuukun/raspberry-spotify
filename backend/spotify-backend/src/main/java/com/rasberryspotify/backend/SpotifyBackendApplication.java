package com.rasberryspotify.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.rasberryspotify")
@EnableJpaRepositories(basePackages = "com.rasberryspotify")
@EntityScan(basePackages = "com.rasberryspotify")
public class SpotifyBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpotifyBackendApplication.class, args);
    }
}
