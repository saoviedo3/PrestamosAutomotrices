# Sistema de Préstamos Automotrices - Módulo de Originación

Este es el módulo de originación del sistema de préstamos automotrices, desarrollado con Spring Boot 3.5.0 y Java 21.

## Características

- **Gestión de Concesionarios**: CRUD completo con validaciones y cambios de estado
- **Gestión de Vendedores**: CRUD completo con relación a concesionarios
- **Historial de Estados**: Auditoría completa de cambios de estado
- **API REST**: Endpoints RESTful siguiendo buenas prácticas
- **Validaciones**: Validaciones de entrada con Bean Validation
- **Manejo de Excepciones**: Excepciones personalizadas y manejo centralizado

## Tecnologías Utilizadas

- Spring Boot 3.5.0
- Java 21
- PostgreSQL
- JPA/Hibernate
- Lombok
- Maven
- Spring Validation

## Estructura del Proyecto

```
src/main/java/com/banquito/sistema/originacion/
├── controller/
│   ├── dto/
│   ├── mapper/
│   └── *Controller.java
├── exception/
├── model/
├── repository/
├── service/
├── config/
└── OriginacionApplication.java
```

## Configuración

### Base de Datos

1. Crear una base de datos PostgreSQL llamada `prestamos_automotrices`
2. Configurar las credenciales en `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/prestamos_automotrices
    username: postgres
    password: postgres
```

### Ejecución

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api`

## Endpoints Disponibles

### Concesionarios

- `GET /api/v1/concesionarios` - Listar todos los concesionarios
- `GET /api/v1/concesionarios?estado=ACTIVO` - Filtrar por estado
- `GET /api/v1/concesionarios?ciudad=Quito` - Filtrar por ciudad
- `GET /api/v1/concesionarios?provincia=Pichincha` - Filtrar por provincia
- `GET /api/v1/concesionarios?nombre=Toyota` - Buscar por nombre
- `GET /api/v1/concesionarios/{id}` - Obtener concesionario por ID
- `GET /api/v1/concesionarios/codigo/{codigo}` - Obtener concesionario por código
- `POST /api/v1/concesionarios` - Crear nuevo concesionario
- `PUT /api/v1/concesionarios/{id}` - Actualizar concesionario
- `PATCH /api/v1/concesionarios/{id}/estado` - Cambiar estado

### Vendedores

- `GET /api/v1/vendedores` - Listar todos los vendedores
- `GET /api/v1/vendedores?estado=ACTIVO` - Filtrar por estado
- `GET /api/v1/vendedores?concesionarioId=1` - Filtrar por concesionario
- `GET /api/v1/vendedores?nombre=Juan` - Buscar por nombre
- `GET /api/v1/vendedores/{id}` - Obtener vendedor por ID
- `GET /api/v1/vendedores/codigo/{codigo}` - Obtener vendedor por código
- `GET /api/v1/vendedores/cedula/{cedula}` - Obtener vendedor por cédula
- `POST /api/v1/vendedores` - Crear nuevo vendedor
- `PUT /api/v1/vendedores/{id}` - Actualizar vendedor
- `PATCH /api/v1/vendedores/{id}/estado` - Cambiar estado

### Historial de Estados

- `GET /api/v1/historial-estados` - Listar todo el historial
- `GET /api/v1/historial-estados?entidadTipo=CONCESIONARIO` - Filtrar por tipo de entidad
- `GET /api/v1/historial-estados?usuarioCambio=admin` - Filtrar por usuario
- `GET /api/v1/historial-estados/{id}` - Obtener historial por ID
- `GET /api/v1/historial-estados/entidad/{tipo}/{id}` - Historial de una entidad específica
- `POST /api/v1/historial-estados` - Crear registro de historial

## Modelos de Datos

### Concesionario

```json
{
  "id": 1,
  "codigo": "CONC001",
  "nombre": "Toyota del Ecuador",
  "direccion": "Av. 6 de Diciembre N24-253",
  "telefono": "022345678",
  "email": "info@toyota.ec",
  "ciudad": "Quito",
  "provincia": "Pichincha",
  "estado": "ACTIVO",
  "fechaCreacion": "2024-01-15T10:30:00",
  "fechaActualizacion": "2024-01-15T10:30:00"
}
```

### Vendedor

```json
{
  "id": 1,
  "codigo": "VEND001",
  "cedula": "1234567890",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez González",
  "telefono": "0987654321",
  "email": "juan.perez@toyota.ec",
  "estado": "ACTIVO",
  "fechaIngreso": "2024-01-15T08:00:00",
  "fechaCreacion": "2024-01-15T10:30:00",
  "fechaActualizacion": "2024-01-15T10:30:00",
  "concesionarioId": 1,
  "concesionarioNombre": "Toyota del Ecuador"
}
```

### Historial de Estado

```json
{
  "id": 1,
  "entidadTipo": "CONCESIONARIO",
  "entidadId": 1,
  "estadoAnterior": "ACTIVO",
  "estadoNuevo": "SUSPENDIDO",
  "motivo": "Incumplimiento de contrato",
  "usuarioCambio": "admin",
  "fechaCambio": "2024-01-15T14:30:00",
  "observaciones": "Suspensión temporal por 30 días"
}
```

## Estados Válidos

### Concesionarios y Vendedores
- **ACTIVO**: Entidad operativa
- **INACTIVO**: Entidad no operativa
- **SUSPENDIDO**: Entidad temporalmente suspendida

### Transiciones de Estado Permitidas
- ACTIVO → INACTIVO, SUSPENDIDO
- INACTIVO → ACTIVO
- SUSPENDIDO → ACTIVO, INACTIVO

## Validaciones

- Códigos únicos para concesionarios y vendedores
- Cédulas únicas para vendedores (10 dígitos numéricos)
- Emails únicos y con formato válido
- Campos obligatorios validados
- Longitudes máximas respetadas

## Manejo de Errores

- **404 Not Found**: Entidad no encontrada
- **400 Bad Request**: Datos duplicados o estados inválidos
- **422 Unprocessable Entity**: Errores de validación

## Desarrollo

### Principios Seguidos

- Clean Architecture
- SOLID Principles
- DRY (Don't Repeat Yourself)
- Separation of Concerns
- RESTful API Design

### Patrones Utilizados

- Repository Pattern
- Service Layer Pattern
- DTO Pattern
- Mapper Pattern
- Exception Handling Pattern 