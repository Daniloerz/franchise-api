### Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar archivos de build primero para aprovechar caché de dependencias
COPY mvnw pom.xml ./
COPY .mvn .mvn

RUN chmod +x mvnw

# Descargar dependencias sin compilar todavía
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src src

# Compilar el proyecto sin ejecutar tests
RUN ./mvnw package -DskipTests -B


### Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Metadata de la imagen
LABEL maintainer="Danilo Ramirez" \
      org.opencontainers.image.title="acc" \
      org.opencontainers.image.description="Franchises Spring Boot API" \
      org.opencontainers.image.version="0.0.1-SNAPSHOT"

# Crear usuario no-root para ejecutar la aplicación
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Ajustar permisos del archivo
RUN chown appuser:appgroup app.jar

# Ejecutar como usuario no-root
USER appuser

EXPOSE 8080

# Healthcheck de la aplicación
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]

