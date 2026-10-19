package com.Taller_apirest.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoTrace API - Sistema de Gestión y Control de Taller Automotriz")
                        .version("1.0.0")
                        .description("Plataforma empresarial de microservicios RESTful con persistencia NoSQL (Spring Data MongoDB) " +
                                "y patrón Database-per-Service. Proporciona trazabilidad completa de servicios, vehículos, repuestos y auditorías de calidad.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo AutoTrace")
                                .email("soporte@autotrace-taller.com")
                                .url("https://github.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("/").description("Servidor Local / Render Cloud")
                ));
    }
}
