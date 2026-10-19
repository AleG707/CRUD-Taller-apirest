package com.Taller_apirest.app.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * Configuración multi-base de datos para implementar el patrón Database-per-Service.
 * Cada microservicio/módulo administra su propia base de datos NoSQL MongoDB de forma aislada.
 */
@Configuration
public class MongoMultiDatabaseConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${database.admin.name:taller_admin_db}")
    private String adminDbName;

    @Value("${database.vehiculos.name:taller_clientes_db}")
    private String vehiculosDbName;

    @Value("${database.ordenes.name:taller_ordenes_db}")
    private String ordenesDbName;

    @Value("${database.proveedores.name:taller_proveedores_db}")
    private String proveedoresDbName;

    @Value("${database.certificaciones.name:taller_certificaciones_db}")
    private String certificacionesDbName;

    /**
     * Crea un MongoClient compartido basado en la URI configurada.
     * Spring Boot auto-configura este bean, pero lo definimos explícitamente
     * para garantizar que todos los módulos usen la misma conexión.
     */
    @Bean
    @Primary
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(settings);
    }

    // --- 1. Módulo Administrador ---
    @Bean(name = "adminMongoDbFactory")
    public MongoDatabaseFactory adminMongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, adminDbName);
    }

    @Bean(name = "adminMongoTemplate")
    public MongoTemplate adminMongoTemplate(MongoDatabaseFactory adminMongoDbFactory) {
        return new MongoTemplate(adminMongoDbFactory);
    }

    // --- 2. Módulo Clientes y Vehículos ---
    @Bean(name = "vehiculosMongoDbFactory")
    public MongoDatabaseFactory vehiculosMongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, vehiculosDbName);
    }

    @Bean(name = "vehiculosMongoTemplate")
    public MongoTemplate vehiculosMongoTemplate(MongoDatabaseFactory vehiculosMongoDbFactory) {
        return new MongoTemplate(vehiculosMongoDbFactory);
    }

    // --- 3. Módulo Órdenes de Trabajo y Servicios ---
    @Bean(name = "ordenesMongoDbFactory")
    public MongoDatabaseFactory ordenesMongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, ordenesDbName);
    }

    @Bean(name = "ordenesMongoTemplate")
    public MongoTemplate ordenesMongoTemplate(MongoDatabaseFactory ordenesMongoDbFactory) {
        return new MongoTemplate(ordenesMongoDbFactory);
    }

    // --- 4. Módulo Proveedores y Reabastecimiento ---
    @Bean(name = "proveedoresMongoDbFactory")
    public MongoDatabaseFactory proveedoresMongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, proveedoresDbName);
    }

    @Bean(name = "proveedoresMongoTemplate")
    public MongoTemplate proveedoresMongoTemplate(MongoDatabaseFactory proveedoresMongoDbFactory) {
        return new MongoTemplate(proveedoresMongoDbFactory);
    }

    // --- 5. Módulo Certificaciones y Auditoría ---
    @Bean(name = "certificacionesMongoDbFactory")
    public MongoDatabaseFactory certificacionesMongoDbFactory(MongoClient mongoClient) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, certificacionesDbName);
    }

    @Bean(name = "certificacionesMongoTemplate")
    public MongoTemplate certificacionesMongoTemplate(MongoDatabaseFactory certificacionesMongoDbFactory) {
        return new MongoTemplate(certificacionesMongoDbFactory);
    }

    // MongoTemplate primario para compatibilidad general
    @Bean(name = "mongoTemplate")
    @Primary
    public MongoTemplate mongoTemplate(@org.springframework.beans.factory.annotation.Qualifier("adminMongoDbFactory") MongoDatabaseFactory adminMongoDbFactory) {
        return new MongoTemplate(adminMongoDbFactory);
    }
}
