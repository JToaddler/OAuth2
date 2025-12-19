package com.appsdeveloperblog.ws.api.ResourceServer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	final String oauth2Password = "oauth2_password";
	final String bearerAuth = "Bearer JWT";

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(bearerAuth))
				.components(new Components().addSecuritySchemes(bearerAuth, new SecurityScheme().name(bearerAuth)
						.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))

				);
	}
}