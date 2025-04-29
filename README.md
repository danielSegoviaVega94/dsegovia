
# Sistema de Gestión de Usuarios - Daniel Segovia

## Descripción

API RESTful implementada con **Spring Boot** para gestión de usuarios, permitiendo operaciones CRUD completas y autenticación mediante JWT.

---

## Requisitos Cumplidos

- ✅ Spring Boot 3.4.5
- ✅ Java 17
- ✅ Base de datos H2 en memoria
- ✅ Spring Data JPA (Hibernate)
- ✅ Maven para gestión de dependencias
- ✅ Autenticación JWT
- ✅ Validaciones con expresiones regulares configurables
- ✅ Documentación Swagger/OpenAPI

---

## Tecnologías Utilizadas

- Spring Boot 3.4.5
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (JSON Web Tokens)
- Lombok
- MapStruct
- Swagger/OpenAPI
- Maven

---

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── ds/evaluacion/dsegovia/
│   │       ├── config/          # Configuraciones
│   │       ├── controller/      # Controladores REST
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── entity/          # Entidades JPA
│   │       ├── exception/       # Excepciones personalizadas
│   │       ├── repository/      # Repositorios JPA
│   │       ├── service/         # Servicios de negocio
│   │       └── validation/      # Validadores personalizados
│   └── resources/
│       └── application.properties
```

---

## Cómo Ejecutar

### Prerrequisitos
- **JDK 17** o superior
- **Maven 3.6+**

### Pasos

**Clonar el repositorio:**
```bash
git clone https://github.com/danielSegoviaVega94/dsegovia.git
cd dsegovia
```

**Compilar el proyecto:**
```bash
mvn clean install
```

**Ejecutar la aplicación:**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: [http://localhost:8080](http://localhost:8080)

---
## Despliegue con Docker

El proyecto incluye un Dockerfile optimizado para facilitar el despliegue en contenedores Docker.

### Prerrequisitos
- Docker instalado en su sistema
- Acceso a la terminal/línea de comandos

### Pasos para despliegue con Docker

**Construir la imagen Docker:**
```bash
docker build -t evaluacion-dsegovia .
```
```bash
docker run --name evaluacionDsegovia -p 8080:8080 evaluacion-dsegovia
```
---
## Consola H2

- **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:userdb`
- **Usuario**: `dsegovia`
- **Contraseña**: `dsegovia`

---

## Documentación API (Swagger)

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Endpoints Disponibles

### Usuarios
- `POST /api/usuarios` - Crear usuario
- `GET /api/usuarios` - Listar usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `PUT /api/usuarios/{id}` - Actualizar usuario completo
- `PATCH /api/usuarios/{id}` - Actualizar usuario parcialmente
- `DELETE /api/usuarios/{id}` - Eliminar usuario

---

## Validaciones

### Formato de Correo
- Debe seguir: `usuario@dominio.com`

### Formato de Contraseña
- Mínimo 8 caracteres
- Al menos una mayúscula
- Al menos una minúscula
- Al menos un número
- Al menos un carácter especial

### Formato de Teléfono (Chile)
- Formato: `+56 9XXXXXXXX` o `2XXXXXXX`

---

## Características de Seguridad
- Autenticación JWT
- Contraseñas encriptadas con BCrypt
- Validación robusta de entrada
- Manejo centralizado de excepciones

---

## Formato de Respuestas

**Respuesta Exitosa:**
```json
{
    "idUsuario": 1,
    "creado": "2025-04-27T10:30:00",
    "modificado": "2025-04-27T10:30:00",
    "ultimoLogin": "2025-04-27T10:30:00",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "activo": true
}
```

**Respuesta de Error:**
```json
{
    "mensaje": "El correo ya está registrado"
}
```

---

## Pruebas con Postman

Se incluye colección Postman en el repositorio:
- Archivo: `API_Gestion_Usuarios_Daniel_Segovia.postman_collection.json`

---

## Diagrama de la Solución

Ver diagrama incluido en este repositorio.
Diagrama.drawio
---

## Notas Adicionales

- Expresiones regulares configurables en `application.properties`.
- Duración de JWT configurable.
- Codificación UTF-8 para caracteres especiales.
