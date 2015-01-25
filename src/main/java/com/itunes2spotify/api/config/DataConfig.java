package com.itunes2spotify.api.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:application.properties")
public class DataConfig {

//    @Resource
//    Environment environment;
//
//    @Bean
//    DataSource dataSource() {
//        DataSourceBuilder factory = DataSourceBuilder
//                .create()
//                .driverClassName(this.environment.getProperty("driverClassName"))
//                .url(this.environment.getProperty("url"))
//                .username(this.environment.getProperty("username"))
//                .password(this.environment.getProperty("password"));
//
//        return factory.build();
//    }
}
