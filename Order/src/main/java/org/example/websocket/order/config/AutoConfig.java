package org.example.websocket.order.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "org.example.websocket.order")
@EntityScan(basePackages = "org.example.websocket.order")
@EnableJpaRepositories(basePackages = "org.example.websocket.order")
public class AutoConfig {
}
