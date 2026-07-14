package com.keax.shared.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI keaxOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Keax Portfolio API")
                        .description("""
                                Documentación OpenAPI del backend de Keax.
                                Incluye endpoints públicos del portfolio y endpoints administrativos protegidos con JWT.
                                """)
                        .version("v1")
                        .contact(new Contact()
                                .name("Keax")
                                .url("https://keax.dev")
                        ))
                .components(new Components()
                        .addSecuritySchemes(BEARER_SCHEME_NAME, new SecurityScheme()
                                .name(BEARER_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Bearer token obtenido desde /api/auth/login")));
    }

    @Bean
    public OpenApiCustomizer securedOperationsCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) {
                return;
            }

            openApi.getPaths().forEach((path, pathItem) -> applySecurity(path, pathItem, PathItem.HttpMethod.GET));
            openApi.getPaths().forEach((path, pathItem) -> applySecurity(path, pathItem, PathItem.HttpMethod.POST));
            openApi.getPaths().forEach((path, pathItem) -> applySecurity(path, pathItem, PathItem.HttpMethod.PUT));
            openApi.getPaths().forEach((path, pathItem) -> applySecurity(path, pathItem, PathItem.HttpMethod.DELETE));
            openApi.getPaths().forEach((path, pathItem) -> applySecurity(path, pathItem, PathItem.HttpMethod.PATCH));
        };
    }

    private static void applySecurity(String path, PathItem pathItem, PathItem.HttpMethod httpMethod) {
        if (pathItem == null) {
            return;
        }

        var operation = pathItem.readOperationsMap().get(httpMethod);

        if (operation == null || isPublicOperation(path, httpMethod)) {
            return;
        }

        operation.addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME_NAME));
    }

    private static boolean isPublicOperation(String path, PathItem.HttpMethod httpMethod) {
        if ("/api/auth/login".equals(path)) {
            return true;
        }

        if (path.startsWith("/api/portfolio/")) {
            return true;
        }

        return "/api/visitor".equals(path) && PathItem.HttpMethod.POST.equals(httpMethod);
    }

}
