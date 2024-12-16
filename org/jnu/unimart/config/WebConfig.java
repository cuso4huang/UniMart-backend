package org.jnu.unimart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = uploadPath;
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
        
        System.out.println("Resource mapping configured: /uploads/** -> " + path);
    }
} 