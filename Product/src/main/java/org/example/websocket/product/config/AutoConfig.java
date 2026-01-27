package org.example.websocket.product.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "org.example.websocket.product")
@EntityScan(basePackages = "org.example.websocket.product")
@EnableJpaRepositories(basePackages = "org.example.websocket.product")
public class AutoConfig {
}
