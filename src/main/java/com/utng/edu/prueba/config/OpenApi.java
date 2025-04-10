package com.utng.edu.prueba.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApi {
    //@Value("${openapi.url.dev}")
    //private String url;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .addServersItem(new Server().
                        url("http://localhost:8035/"));
    }
}
