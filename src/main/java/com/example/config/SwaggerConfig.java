package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        Contact contact = new Contact("Rocket", "https://github.com/VitRocket", "");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo("Api", "Api Documentation", "1.0.0", "", contact, "", "", Collections.emptyList()))
                .select()
                .paths(PathSelectors.regex("(/products.*)|(/actuator.*)"))
                .build();
    }

}
