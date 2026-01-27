package org.example.websocket.inventory.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "org.example.websocket.inventory")
@EntityScan(basePackages = "org.example.websocket.inventory")
@EnableJpaRepositories(basePackages = "org.example.websocket.inventory")
public class AutoConfig {
}
