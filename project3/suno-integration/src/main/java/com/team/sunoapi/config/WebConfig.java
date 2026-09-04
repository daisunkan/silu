package com.team.sunoapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 获取当前工作目录
        String currentDir = System.getProperty("user.dir");
        String uploadPath = "file:" + currentDir + File.separator + "uploads" + File.separator;

        // 允许访问uploads目录下的文件，映射路径为 /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);

        System.out.println("静态资源映射配置: " + uploadPath);
    }

   @Override  // 确保有这个注解
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:8083")  // 改为你的前端端口
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 添加 OPTIONS
                .allowedHeaders("*")  // 允许所有头
                .allowCredentials(true)
                .maxAge(3600);  // 预检请求缓存时间
    }
}