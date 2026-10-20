# Etapa 1: Build con Maven y JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar descriptor de dependencias
COPY pom.xml .
# Descargar dependencias offline para optimizar caché
RUN mvn dependency:go-offline -B || true

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen liviana de ejecución con JRE 17
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Crear usuario sin privilegios por seguridad
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copiar el artefacto generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Variables de entorno por defecto
ENV PORT=8080
EXPOSE 8080

# Comando de inicio del microservicio con forzado IPv4 para resolucion DNS con MongoDB Atlas
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Djava.net.preferIPv4Stack=true", "-jar", "app.jar"]
