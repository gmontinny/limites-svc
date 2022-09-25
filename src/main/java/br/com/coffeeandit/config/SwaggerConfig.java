package br.com.coffeeandit.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo("Coffee and IT - Spring",
                        "Api de limites.",
                        "1.0.0",
                        "",
                        null,
                        "",
                        "",
                        new ArrayList<>()
                ))
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.coffeeandit.api"))
                .paths(PathSelectors.any())
                .build();
    }
}
