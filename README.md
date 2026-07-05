# portfolio-backend

## Configuracion de entornos

La aplicacion ahora separa configuracion por perfil de Spring:

- `prod` carga el archivo `.env`
- `dev` carga el archivo `.env.dev`

La aplicacion no selecciona un perfil por defecto. Debes declarar
`SPRING_PROFILES_ACTIVE` de forma explicita para evitar que una instancia de
produccion cargue accidentalmente valores de desarrollo.

## Arranque local

En PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
.\mvnw.cmd spring-boot:run
```

## Despliegue

En produccion define el perfil `prod` en el proceso o servicio:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
```

Luego completa el archivo `.env` con los valores reales del entorno de despliegue.

## Verificacion

Ejecuta la compilacion, las pruebas y las reglas de arquitectura con:

```powershell
.\mvnw.cmd clean verify
```

El reporte de cobertura se genera en `target/site/jacoco/index.html`.
El build exige al menos 25% de cobertura de lineas como umbral inicial; debe
subirse gradualmente al incorporar pruebas de los casos de uso CRUD restantes.
