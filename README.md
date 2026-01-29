# Franchise API

API REST para gestión de franquicias, sucursales y productos.

## Tecnologías

- Java 17
- Spring Boot 3.5.x
- PostgreSQL 15
- Docker & Docker Compose
- OpenAPI/Swagger para documentación

## Arquitectura

El proyecto sigue Clean Architecture con las siguientes capas:

- **domain**: Modelos de negocio, interfaces de repositorio y excepciones
- **application**: Servicios (casos de uso) y DTOs
- **infrastructure**: Implementaciones JPA, entidades de persistencia y configuración
- **web**: Controllers REST y manejo de excepciones HTTP

## Requisitos Previos

- Docker y Docker Compose instalados
- (Opcional) Java 17 y Maven para desarrollo local

## Ejecución con Docker

1. Clonar el repositorio:
   ```bash
   git clone <url-del-repositorio>
   cd <nombre-del-proyecto>
   ```

2. Ejecutar con Docker Compose:
   ```bash
   docker-compose up --build
   ```

3. La API estará disponible en: http://localhost:9090  
4. Documentación Swagger: http://localhost:9090/swagger-ui.html

## Ejecución Local (sin Docker)

1. Tener PostgreSQL corriendo en localhost:5432
2. Crear base de datos: `franchise_db`
3. Ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```

## Endpoints

### Franquicias

| Método | Endpoint                                         | Descripción                              |
|--------|--------------------------------------------------|------------------------------------------|
| POST   | /api/v1/franchises                              | Crear franquicia                         |
| PATCH  | /api/v1/franchises/{id}/name                    | Actualizar nombre                        |
| GET    | /api/v1/franchises/{id}/top-stock-products      | Productos con más stock por sucursal    |

### Sucursales

| Método | Endpoint                                                          | Descripción         |
|--------|-------------------------------------------------------------------|---------------------|
| POST   | /api/v1/franchises/{franchiseId}/branches                        | Agregar sucursal    |
| PATCH  | /api/v1/franchises/{franchiseId}/branches/{branchId}/name        | Actualizar nombre   |

### Productos

| Método | Endpoint                                                          | Descripción         |
|--------|-------------------------------------------------------------------|---------------------|
| POST   | /api/v1/branches/{branchId}/products                              | Agregar producto    |
| DELETE | /api/v1/branches/{branchId}/products/{productId}                  | Eliminar producto   |
| PATCH  | /api/v1/branches/{branchId}/products/{productId}/stock            | Actualizar stock    |
| PATCH  | /api/v1/branches/{branchId}/products/{productId}/name             | Actualizar nombre   |

## Ejemplos de Uso (cURL)

### Crear franquicia
```bash
curl -X POST http://localhost:9090/api/v1/franchises \
  -H "Content-Type: application/json" \
  -d '{"name": "Mi Franquicia"}'
```

### Agregar sucursal
```bash
curl -X POST http://localhost:9090/api/v1/franchises/{franchiseId}/branches \
  -H "Content-Type: application/json" \
  -d '{"name": "Sucursal Centro"}'
```

### Agregar producto
```bash
curl -X POST http://localhost:9090/api/v1/branches/{branchId}/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Producto A", "stock": 100}'
```

### Obtener productos con más stock por sucursal
```bash
curl http://localhost:9090/api/v1/franchises/{franchiseId}/top-stock-products
```

## Ejecutar Tests

```bash
./mvnw test
```

## Estructura del Proyecto

```text
src/
├── main/
│   ├── java/com/franchises/acc/
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── web/
│   └── resources/
│       └── application.yml
└── test/
    ├── java/com/franchises/acc/
    └── resources/
        └── application-test.yml
```

## Decisiones Técnicas

1. **Clean Architecture**: Separación clara entre dominio, aplicación e infraestructura
2. **UUIDs**: Identificadores universales para todas las entidades
3. **DTOs separados**: Request y Response DTOs distintos para flexibilidad
4. **Validaciones**: Jakarta Validation en DTOs de entrada
5. **Manejo de errores**: `GlobalExceptionHandler` con respuestas consistentes

