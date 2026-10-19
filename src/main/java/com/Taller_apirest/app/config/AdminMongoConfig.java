package com.Taller_apirest.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.Taller_apirest.app.modules.admin.repository",
        mongoTemplateRef = "adminMongoTemplate"
)
public class AdminMongoConfig {
}
