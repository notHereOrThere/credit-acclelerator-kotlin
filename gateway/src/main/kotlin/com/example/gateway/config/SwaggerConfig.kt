package com.example.gateway.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    private fun createAPIKeyScheme(): SecurityScheme? {
        return SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
    }

    @Bean
    fun openAPI(): OpenAPI? {
        return OpenAPI().addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
            .components(Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))

    }
}