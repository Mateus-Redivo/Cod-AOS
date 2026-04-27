package com.aos.productsapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for the application.
 * Defines which origins, methods, and headers are permitted to access the API.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Registers CORS mappings for all product endpoints.
     *
     * @param registry the CORS registry to configure
     */
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
