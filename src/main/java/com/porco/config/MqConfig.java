package com.porco.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class MqConfig {

    @Bean
    public DemoConfig zmDemoConfig() {
        log.info("1");
        return new DemoConfig("zmDemoConfig");
    }

    @Bean
    @Primary
    @Scope("prototype")
    public DemoConfig demoConfig() {
        log.info("2");
        return new DemoConfig("demoConfig");
    }

    @Getter
    @Setter
    @ToString
    public static class DemoConfig {
        private String name;

        public DemoConfig(String name) {
            log.info(name);
            this.name = name;
        }

        public DemoConfig() {
            log.info(name);
        }
    }
}
