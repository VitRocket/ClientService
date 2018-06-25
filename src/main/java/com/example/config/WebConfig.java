package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig {

    @Bean
    WebMvcConfigurer configurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/bootstrap/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/")
                        .setCacheControl(CacheControl.maxAge(30L, TimeUnit.DAYS).cachePublic());

                registry.addResourceHandler("/jquery/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/jquery/")
                        .setCacheControl(CacheControl.maxAge(30L, TimeUnit.DAYS).cachePublic());
            }
        };
    }

}
