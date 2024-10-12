package org.example.footballanalyzer.Config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("football app")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Football analyzer").version("1.0")))
                .build();
    }
}
