package com.poultry.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

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

        String targetUrl = (explicitUrl != null && !explicitUrl.isBlank()) ? explicitUrl.trim() : url.trim();
        String targetUser = System.getenv("SPRING_DATASOURCE_USERNAME") != null ? System.getenv("SPRING_DATASOURCE_USERNAME").trim() : username.trim();
        String targetPass = System.getenv("SPRING_DATASOURCE_PASSWORD") != null ? System.getenv("SPRING_DATASOURCE_PASSWORD") : password;

        boolean isPlaceholder = targetUrl.contains("<") || targetUrl.contains(">")
                || targetUrl.contains("your-cloud") || targetUrl.contains("placeholder")
                || targetUrl.contains("example.com");

        if (isPlaceholder || (renderEnv != null && targetUrl.contains("localhost"))) {
            log.warn("Database URL '{}' contains placeholders or localhost on Render. Activating embedded database so service boots cleanly and stays live.", targetUrl);
            return createH2DataSource();
        }

        if (targetUrl.startsWith("jdbc:mysql:")) {
            try {
                DriverManager.setLoginTimeout(3);
                try (Connection conn = DriverManager.getConnection(targetUrl, targetUser, targetPass)) {
                    log.info("Database connection verified successfully.");
                }
            } catch (Exception e) {
                log.error("Unable to connect to MySQL database at '{}': {}. Falling back to embedded database to prevent deployment crash.", targetUrl, e.getMessage());
                return createH2DataSource();
            }
        }

        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.url(targetUrl);
        builder.username(targetUser);
        builder.password(targetPass);

        if (targetUrl.startsWith("jdbc:mysql:")) {
            builder.driverClassName("com.mysql.cj.jdbc.Driver");
        } else if (targetUrl.startsWith("jdbc:h2:")) {
            builder.driverClassName("org.h2.Driver");
        }

        return builder.build();
    }

    private DataSource createH2DataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create();
        builder.driverClassName("org.h2.Driver");
        builder.url("jdbc:h2:file:./data/poultry_db;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        builder.username("sa");
        builder.password("");
        return builder.build();
    }
}
