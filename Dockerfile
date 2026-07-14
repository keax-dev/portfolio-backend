# Etapa 1: compilar la aplicacion Spring Boot y generar el jar listo para produccion.
FROM maven:3.9.11-eclipse-temurin-21 AS build

# Carpeta de trabajo comun para toda la etapa de build.
WORKDIR /app

# Copia primero wrapper y descriptor para aprovechar cache de dependencias.
COPY .mvn .mvn
COPY mvnw pom.xml ./

# En Windows el wrapper puede quedar con CRLF.
# Se normaliza el script y luego se descargan dependencias por adelantado.
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw \
    && ./mvnw -B dependency:go-offline

# Copia el resto del codigo fuente una vez resueltas las dependencias.
COPY src src

# Empaqueta la aplicacion sin ejecutar tests dentro de Docker.
# Los tests ya deben haber pasado previamente en el workflow de CI.
RUN ./mvnw -B clean package -DskipTests

# Etapa 2: imagen ligera de ejecucion solamente con JRE.
FROM eclipse-temurin:21-jre-jammy

# Instala curl para el healthcheck del contenedor.
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Carpeta donde vivira el jar dentro del contenedor.
WORKDIR /app

# Copia el artefacto generado en la etapa anterior.
COPY --from=build /app/target/*.jar /app/app.jar

# Puerto por defecto cuando la app corre con configuracion estandar.
# En produccion el compose puede sobrescribir PORT, por ejemplo a 9090.
EXPOSE 8080

# Healthcheck local del contenedor.
# Si Actuator deja de responder, Docker podra marcar la instancia como unhealthy.
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=5 \
  CMD sh -c 'curl --fail --silent http://127.0.0.1:${PORT:-8080}/actuator/health || exit 1'

# Permite inyectar ajustes de JVM desde el entorno del servidor si luego lo necesitas.
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
