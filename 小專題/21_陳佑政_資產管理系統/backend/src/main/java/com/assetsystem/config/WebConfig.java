package com.assetsystem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 允許 React (Vite) 前端跨來源呼叫 /api */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors-origins}")
    private String[] corsOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(corsOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
