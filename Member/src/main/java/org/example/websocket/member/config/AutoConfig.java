package org.example.websocket.member.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "org.example.websocket.member")
@EntityScan(basePackages = "org.example.websocket.member")
@EnableJpaRepositories(basePackages = "org.example.websocket.member")
public class AutoConfig {
}
