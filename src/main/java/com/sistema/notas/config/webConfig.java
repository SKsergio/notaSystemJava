package com.sistema.notas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class webConfig implements WebMvcConfigurer {

    @Value("${app.storage.photo-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        String finalUri = uploadPath.toUri().toString();

        registry.addResourceHandler("/photos/**")
                .addResourceLocations(finalUri);
    }
}
