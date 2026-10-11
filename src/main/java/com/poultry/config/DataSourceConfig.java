package com.poultry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username:root}")
    private String username;

    @Value("${spring.datasource.password:ROOT}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        String renderEnv = System.getenv("RENDER");
        String explicitUrl = System.getenv("SPRING_DATASOURCE_URL");

        // If running on Render and the user hasn't configured a cloud MySQL URL yet
        if (renderEnv != null && (explicitUrl == null || explicitUrl.isBlank() || explicitUrl.contains("localhost"))) {
            log.warn("Detected Render deployment without external cloud database URL! Falling back to embedded H2 database so service stays healthy and live.");
            DataSourceBuilder<?> builder = DataSourceBuilder.create();
            builder.driverClassName("org.h2.Driver");
            builder.url("jdbc:h2:file:./data/poultry_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
            builder.username("sa");
            builder.password("");
            return builder.build();
        }

        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(url);
        builder.username(username);
        builder.password(password);

        if (url.startsWith("jdbc:mysql:")) {
            builder.driverClassName("com.mysql.cj.jdbc.Driver");
        } else if (url.startsWith("jdbc:h2:")) {
            builder.driverClassName("org.h2.Driver");
        }

        return builder.build();
    }
}
