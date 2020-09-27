package com.example.connect4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	
	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Connect4 Game")
                        .description(" List of APIs in Connect4 Game using springdoc-openapi and OpenAPI 3."));
    }
	
	/**
	 * ObjectMapper for entity to DTO conversion and vice versa
	 * 
	 * @return ModelMapper bean
	 */
	@Bean
	public ObjectMapper getOjectMapper() {
		return new ObjectMapper();
	}

}
