package com.Taller_apirest.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.Taller_apirest.app.modules.certificaciones.repository",
        mongoTemplateRef = "certificacionesMongoTemplate"
)
public class CertificacionesMongoConfig {
}
