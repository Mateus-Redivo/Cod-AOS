package com.aos.productsapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Configuração global de CORS: define quais origens, métodos e headers podem acessar a API
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/products/**")
                // Allowed frontend origins — add production URL when deploying
                .allowedOrigins("http://localhost:3000", "http://localhost:8080")
                // Only the HTTP methods used by the products API
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // Standard headers required for JSON requests with auth
                .allowedHeaders("Content-Type", "Authorization")
                // Allows cookies and auth tokens to be sent cross-origin
                .allowCredentials(true)
                // Browser caches the preflight response for 1 hour
                .maxAge(3600);
    }
}
