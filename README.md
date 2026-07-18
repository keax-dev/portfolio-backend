<p align="right">
  <a href="./README.md">
    <img src="https://img.shields.io/badge/README-Espa%C3%B1ol-1f6feb?style=for-the-badge" alt="README en Español" />
  </a>
  <a href="./README.en.md">
    <img src="https://img.shields.io/badge/README-English-0e8a16?style=for-the-badge" alt="README in English" />
  </a>
</p>

# Portfolio Backend

API backend del portafolio público y panel administrativo, construida con Spring Boot 3.5, Java 21, arquitectura hexagonal, PostgreSQL, Flyway, OpenAPI/Swagger, Actuator, Docker y despliegue automatizado con GitHub Actions.

## Resumen funcional

Este backend expone dos grupos de capacidades:

- Endpoints públicos para renderizar el portafolio, registrar visitantes y enviar mensajes de contacto.
- Endpoints administrativos protegidos con JWT para gestionar perfil, educación, habilidades, tecnologías, proyectos, instituciones, redes sociales e imágenes.

Rutas principales:

- `/api/auth/login`: autenticación administrativa.
- `/api/portfolio/*`: consumo público del portafolio.
- `/api/profile`, `/api/education`, `/api/skill`, `/api/technology`, `/api/project`, `/api/institution`, `/api/socialNetwork`: CRUD administrativo.
- `/api/image/*`: carga y eliminación de imágenes.
- `/api/visitor` y `/api/visitor/dashboard`: registro y consulta de visitantes.

## Qué está implementado

### API de autenticación y seguridad

- Login con JWT para el panel administrativo.
- Filtro de autenticación basado en token.
- Configuración de seguridad HTTP y headers.
- Protección de endpoints administrativos y separación entre rutas públicas y privadas.

### API pública del portafolio

- Consulta del perfil principal.
- Consulta de educación, habilidades, tecnologías, proyectos unificados y redes sociales.
- Proyectos públicos con títulos y descripciones localizadas, tecnologías, links e imágenes ordenadas.
- Envío de formulario de contacto.
- Registro de visitantes con deduplicación por ventana de tiempo.
- Resolución de IP cliente con soporte controlado para proxys.

### API administrativa

- CRUD de perfil.
- CRUD de educación.
- CRUD de instituciones.
- CRUD de habilidades.
- CRUD de tecnologías por nombre, sin una posición global en el catálogo.
- CRUD de proyectos con una o varias tecnologías y links ordenados por proyecto.
- CRUD de redes sociales.
- Carga múltiple y eliminación controlada de imágenes de proyectos en Cloudinary.
- Dashboard de visitantes.

### Modelo y reglas de proyectos

- Cada registro representa un proyecto completo; frontend y backend ya no se almacenan como proyectos independientes.
- `technologies` requiere al menos una tecnología existente. Los IDs de tecnología y las posiciones deben ser únicos dentro del proyecto.
- La posición de una tecnología pertenece a la relación proyecto-tecnología; el catálogo global de tecnologías conserva solamente `id`, `name` y `deleted`.
- `links` puede estar vacío y admite `DEPLOY`, `GITHUB`, `GITHUB_FRONTEND` y `GITHUB_BACKEND`. Tipo y posición no pueden repetirse dentro del proyecto.
- `images` reemplaza al campo legado `picture` y devuelve hasta tres imágenes con `id`, `url` y `position`.
- La carga usa `POST /api/image/project/{projectId}` con uno o más archivos multipart bajo `images`, sin superar tres imágenes acumuladas.
- La eliminación usa `DELETE /api/image/project/{projectId}/{projectImageId}` y evita dejar un proyecto sin imágenes.
- Los cambios de orden de tecnologías y links se persisten en una sola transacción, evitando colisiones temporales con sus restricciones de posición única.

Representación resumida devuelta por la API:

```json
{
  "id": 4,
  "title": "COURIER OPERATIONS PLATFORM",
  "title_es": "PLATAFORMA DE OPERACIONES COURIER",
  "description": "...",
  "description_es": "...",
  "position": 1,
  "technologies": [
    {
      "relation_id": 1,
      "id": 1,
      "name": "ANGULAR",
      "position": 1
    }
  ],
  "links": [
    {
      "id": 1,
      "type": "GITHUB_FRONTEND",
      "url": "https://github.com/...",
      "position": 1
    }
  ],
  "images": [
    {
      "id": 1,
      "url": "https://res.cloudinary.com/...",
      "position": 1
    }
  ],
  "deleted": false
}
```

### Infraestructura y observabilidad

- Migraciones versionadas con Flyway.
- Documentación OpenAPI y Swagger UI.
- Healthchecks y métricas con Spring Boot Actuator.
- Exportación Prometheus.
- Correlation ID en requests.
- Manejo global de errores.
- Logging configurable por variables de entorno.

## Arquitectura del proyecto

El proyecto está organizado con enfoque hexagonal:

- `application`: casos de uso.
- `domain`: modelos y puertos.
- `infrastructure`: controladores web, adaptadores de persistencia, seguridad, correo, almacenamiento y configuración.
- `shared`: excepciones, filtros y configuraciones transversales.

Cada módulo funcional sigue la misma idea. Por ejemplo:

- `profile`, `education`, `project`, `technology`, `skill`, `institution`, `socialnetwork`, `visitor`, `auth`, `email`, `uploadimage`.

Esto permite:

- Aislar la lógica de negocio,
- Probar casos de uso sin depender de Spring MVC,
- Reemplazar adaptadores sin romper el dominio,
- Validar reglas de arquitectura con ArchUnit.

## Stack técnico

- Java 21
- Spring Boot 3.5.15
- Spring Web
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Mail
- Thymeleaf
- PostgreSQL
- Flyway
- springdoc OpenAPI + Swagger UI
- Spring Boot Actuator
- Micrometer Prometheus
- Cloudinary
- JWT (`jjwt`)
- JUnit 5
- Mockito
- H2
- Testcontainers
- ArchUnit
- Docker
- GitHub Actions

## Perfiles y configuración por entorno

La aplicación no define un perfil por defecto. Esto se dejó así para evitar que un despliegue productivo cargue valores de desarrollo por accidente.

Perfiles disponibles:

- `dev`: importa `./.env.dev`
- `prod`: importa `./.env`

Archivos relevantes:

- `src/main/resources/application.properties`: Configuración base expresada con variables de entorno.
- `src/main/resources/application-dev.properties`: Importa `.env.dev`.
- `src/main/resources/application-prod.properties`: Importa `.env`.
- `.env.example`: Plantilla de referencia para construir archivos reales.
- `.env.dev`: Valores locales de desarrollo.
- `.env`: Valores de producción cuando el backend corre con perfil `prod`.

## Requisitos

Para desarrollo local:

- Java 21
- PostgreSQL disponible localmente
- acceso a las variables requeridas en `.env.dev`

Para validación y build:

- Maven Wrapper (`mvnw`) ya incluido en el repositorio

## Ejecución local

### Desde PowerShell

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

### Desde IntelliJ IDEA

En la configuración de ejecución de Spring Boot:

- `Active profiles`: `dev`

Así Spring cargará `application-dev.properties`, y ese archivo importará `.env.dev`.

### Build local

```powershell
.\mvnw.cmd clean package
```

## Variables de entorno

El backend usa variables para:

- Servidor HTTP
- Datasource y pool Hikari
- Hibernate y Flyway
- CORS
- Logging
- Actuator y Prometheus
- OpenAPI/Swagger
- Cloudinary
- Correo SMTP
- JWT
- Rate limiting de contacto
- Deduplicación de visitantes

La plantilla base está en:

- `.env.example`

## Base de datos y migraciones

Flyway quedó integrado como mecanismo oficial de versionado del esquema.

Migraciones actuales:

- `src/main/resources/db/migration/V1__init_schema.sql`
- `src/main/resources/db/migration/V2__project_technologies_and_links.sql`
- `src/main/java/db/migration/V3__merge_split_projects.java`
- `src/main/java/db/migration/V4__project_images.java`
- `src/main/resources/db/migration/V5__drop_technology_position.sql`

Estas migraciones crean las relaciones de tecnologías y links, unifican proyectos previamente separados, migran las imágenes al arreglo ordenado y eliminan la posición global del catálogo de tecnologías.

Configuración relevante:

- `spring.flyway.enabled=true`
- `spring.flyway.baseline-on-migrate=true`

Esto permite:

- Inicializar entornos nuevos de forma consistente,
- Versionar cambios de esquema junto al código,
- Adoptar Flyway sobre una base existente sin borrar datos en la primera migración si el entorno ya estaba creado.

## Documentación y observabilidad

### Swagger UI

Con la aplicación levantada:

- `http://localhost:8080/swagger-ui.html`

### OpenAPI JSON

- `http://localhost:8080/v3/api-docs`

### Actuator

- `http://localhost:8080/actuator/health`
- `http://localhost:8080/actuator/metrics`
- `http://localhost:8080/actuator/prometheus`

### Qué aportan estos endpoints

- `health`: Sirve para healthchecks locales, Docker, Nginx o balanceadores.
- `metrics` y `prometheus`: Sirven para monitoreo y scraping.
- `swagger-ui`: Facilita revisar y probar la API sin Postman.

## Pruebas y calidad

La validación principal del backend se ejecuta con:

```powershell
.\mvnw.cmd clean verify
```

Ese comando cubre:

- Compilación
- Pruebas unitarias
- Pruebas de integración
- Pruebas de arquitectura
- Reporte JaCoCo
- Validación de cobertura mínima

### Tipos de pruebas que ya existen

- Pruebas de casos de uso: `*UseCasesTest`, `*UseCaseImplTest`
- Pruebas de seguridad: `SecurityConfigTest`, `SecurityHeadersTest`, `JwtAuthenticationFilterTest`
- Pruebas de controladores e integración web: `*ApiIntegrationTest`
- Pruebas de persistencia: `Jpa*RepositoryIntegrationTest`, `PostgreSqlPersistenceIntegrationTest`
- Pruebas de arquitectura: `HexagonalArchitectureTest`
- Pruebas de mapeo y contratos: `MapperContractsTest`
- Pruebas de reglas estructurales de proyectos: `ProjectStructureValidatorTest`
- Pruebas de migración de proyectos e imágenes: `V3MergeSplitProjectsTest`, `V4ProjectImagesTest`
- Pruebas H2 y PostgreSQL para eliminar y reordenar relaciones sin conflictos de integridad.
- Pruebas end-to-end backend: `BackendEndToEndTest`
- Pruebas de observabilidad y documentación: `ActuatorApiIntegrationTest`, `ManagementApiIntegrationTest`, `OpenApiIntegrationTest`

### Cobertura

El build exige actualmente:

- Mínimo `75%` de cobertura de líneas
- Mínimo `50%` de cobertura de branches

Reporte generado en:

- `target/site/jacoco/index.html`

Además, Maven Enforcer valida:

- Java 21
- Maven 3.9+

## Docker

El backend incluye un `Dockerfile` multi-stage:

- Etapa 1: compila con Maven + Temurin 21
- Etapa 2: ejecuta con JRE liviano

Características de la imagen:

- Instala `curl` para healthchecks internos,
- Expone `8080`,
- Permite inyectar `JAVA_OPTS`,
- Usa `/actuator/health` como healthcheck del contenedor.

Build local de ejemplo:

```powershell
docker build -t portfolio-backend:local .
```

## Despliegue productivo con Docker

El despliegue productivo está pensado para Lightsail con Docker Compose.

Archivo principal:

- `docker-compose.prod.yml`

Modelo actual:

- La imagen se publica en GHCR,
- El servidor descarga la imagen exacta por tag `sha-*`,
- El contenedor usa `network_mode: host`,
- El backend queda accesible en `127.0.0.1:8080`,
- Nginx actúa como reverse proxy público.

Carpeta de despliegue esperada:

- `/opt/portfolio-backend`

Archivos esperados ahí:

- `.env`: variables reales del backend
- `.env.prod`: referencia de imagen generada por el workflow
- `docker-compose.prod.yml`

### Ejemplo operativo en servidor

```bash
cd /opt/portfolio-backend
docker compose --env-file .env.prod -f docker-compose.prod.yml up -d
```

## CI/CD

El proyecto ya incluye automatización completa en GitHub Actions.

### 1. CI

Archivo:

- `.github/workflows/ci.yml`

Qué hace:

- Corre en `push` a `main`
- Corre en `pull_request` hacia `main`
- Ejecuta `./mvnw -B verify`
- Valida que la imagen Docker del backend construya correctamente
- Sube artefactos de pruebas si algo falla

### 2. Deploy Prod

Archivo:

- `.github/workflows/deploy-prod.yml`

Qué hace:

- Se dispara cuando `CI` termina correctamente sobre `main`
- Publica imagen en GHCR
- Etiqueta la imagen como `latest` y `sha-<commit>`
- Despliega en Lightsail usando self-hosted runner
- Valida el arranque con `curl http://127.0.0.1:8080/actuator/health`

### 3. Rollback Prod

Archivo:

- `.github/workflows/rollback-prod.yml`

Qué hace:

- Se ejecuta manualmente
- Recibe un `image_tag`
- Vuelve a desplegar una imagen previa ya publicada en GHCR
- Verifica nuevamente el healthcheck al finalizar

## Configuración de GitHub necesaria

### Environment

- `production`

### Secrets usados por despliegue

- `LIGHTSAIL_BACKEND_PATH`
- `GHCR_READ_TOKEN`

### Runner self-hosted esperado

Labels:

- `self-hosted`
- `Linux`
- `X64`
- `production`

## Estructura resumida

```text
src/
  main/
    java/com/keax/
      auth/
      education/
      email/
      institution/
      portfolio/
      profile/
      project/
      shared/
      skill/
      socialnetwork/
      technology/
      uploadimage/
      visitor/
    resources/
      application.properties
      application-dev.properties
      application-prod.properties
      db/migration/
  test/
    java/com/keax/
      architecture/
      e2e/
      management/
      persistence/
      ...
```

## Estado actual del backend

Hoy el proyecto ya cuenta con:

- Arquitectura hexagonal validada por pruebas,
- Seguridad JWT,
- Documentación Swagger,
- Observabilidad con Actuator y Prometheus,
- Migraciones con Flyway,
- Proyectos unificados con tecnologías, links e imágenes ordenadas,
- Reordenamiento transaccional de relaciones validado en PostgreSQL,
- Cobertura mínima obligatoria,
- Contenedor Docker multi-stage,
- Pipeline de CI,
- Deploy automatizado a producción,
- Rollback manual por tag de imagen.

## Comandos útiles

### Verificación completa

```powershell
.\mvnw.cmd clean verify
```

### Ejecutar local con perfil de desarrollo

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

### Construir imagen Docker

```powershell
docker build -t portfolio-backend:local .
```
