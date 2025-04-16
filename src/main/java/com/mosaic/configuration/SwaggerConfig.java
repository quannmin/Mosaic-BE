package com.mosaic.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mosaic Store API Documentation")
                        .description("""
                                <h2>Mosaic Store - Premium T-Shirt Ecommerce Platform</h2>
                                <p>This API serves as the backend for our T-shirt ecommerce platform, offering comprehensive endpoints for product management, user authentication, order processing, and more.</p>
                                
                                <h3>Key Features:</h3>
                                <ul>
                                  <li>Product catalog management with image uploads</li>
                                  <li>User authentication and profile management</li>
                                  <li>Shopping cart functionality</li>
                                  <li>Order processing and history</li>
                                  <li>Secure payment integration</li>
                                </ul>
                                
                                <h3>Getting Started:</h3>
                                <p>Authenticate using JWT tokens for protected endpoints. Public endpoints can be accessed without authentication.</p>
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("QuanNM")
                                .email("louisquinn296@gmail.com")
                                .url("https://mosaic-store.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("JWT Authentication"))
                .components(new Components()
                        .addSecuritySchemes("JWT Authentication", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token in the format: Bearer {token}")));
    }
}