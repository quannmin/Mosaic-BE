package com.mosaic.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Mosaic Store - Ecommerce platform for selling T-shirt")
                        .description("A T-shirt store")
                        .version("1.0")
                        .contact(new Contact()
                                .name("QuanNM")
                                .email("louisquinn296@gmail.com")))
                .addSecurityItem(new SecurityRequirement().addList("Mosaic Authentication Service"))
                .components(new Components().addSecuritySchemes("Mosaic Authentication Service", new SecurityScheme()
                        .name("Mosaic Authentication Service").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
