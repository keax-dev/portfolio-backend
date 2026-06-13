# portfolio-backend

## Configuracion de entornos

La aplicacion ahora separa configuracion por perfil de Spring:

- `prod` carga el archivo `.env`
- `dev` carga el archivo `.env.dev`

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
