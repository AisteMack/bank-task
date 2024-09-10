package com.example.bank.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Bank"));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            List<Parameter> globalHeaders = new ArrayList<>();
            globalHeaders.add(new Parameter()
                    .name("X-UserName")
                    .in("header")
                    .required(true)
                    .schema(new io.swagger.v3.oas.models.media.StringSchema()));

            openApi.getPaths().forEach((path, pathItem) -> pathItem.readOperations().forEach(operation -> {
                if (operation.getParameters() == null) {
                    operation.setParameters(globalHeaders);
                } else {
                    operation.getParameters().addAll(globalHeaders);
                }
            }));
        };
    }
}