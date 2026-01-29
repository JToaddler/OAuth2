package com.appsdeveloperblog.ws.api.ResourceServer.security;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Resource Server API ", description = "description"))
@io.swagger.v3.oas.annotations.security.SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER, scheme = "basic")
public class SwaggerConfig {

	@Value("${spring.security.oauth2.client.provider.oidc.token-uri}")
	private String tokenUrl;

	@Value("${spring.security.oauth2.client.registration.oidc.scope}")
	private String oidcScope;

	@Value("${spring.security.oauth2.client.provider.oidc-provider.authorization-uri}")
	private String authorizationUrl;

	final String oauth2Password = "oauth2_password";
	final String bearerAuth = "Bearer JWT";

	@Bean
	GroupedOpenApi internalApi() {
		return GroupedOpenApi.builder().group("Resource Server API")
				.packagesToScan("com.appsdeveloperblog.ws.api.ResourceServer.controllers").build();
	}

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("OIDC"))
				.addSecurityItem(
						new SecurityRequirement().addList("JWT"))
				.components(new io.swagger.v3.oas.models.Components()

						.addSecuritySchemes("JWT",
								new io.swagger.v3.oas.models.security.SecurityScheme()
										.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
										.scheme("bearer").bearerFormat("JWT"))

						.addSecuritySchemes("basicAuth", new io.swagger.v3.oas.models.security.SecurityScheme()
								.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP).scheme("basic")
								.flows(new OAuthFlows().password(new OAuthFlow()
										.authorizationUrl(authorizationUrl).tokenUrl(tokenUrl)
										.scopes(new Scopes().addString(oidcScope, "Token Service Scope"))))
								)

						.addSecuritySchemes("OIDC",
								new io.swagger.v3.oas.models.security.SecurityScheme()
										.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.OAUTH2)
										.flows(new OAuthFlows().authorizationCode(new OAuthFlow()
												.authorizationUrl(authorizationUrl).tokenUrl(tokenUrl)
												.scopes(new Scopes().addString(oidcScope, "Token Service Scope"))))));
	}

	@Bean
	OpenApiCustomizer customTagSort() {
		return openApi -> {
			if (openApi.getTags() != null && !openApi.getTags().isEmpty()) {
				openApi.setTags(openApi.getTags().stream()
						.sorted((Tag t1, Tag t2) -> t1.getName().compareToIgnoreCase(t2.getName()))
						.collect(Collectors.toList()));
			}
		};
	}

	@Bean
	OperationCustomizer disableSecurityForInternalToken() {
		return (operation, handlerMethod) -> {
			if (handlerMethod.getMethod().getName().equals("token")) {
				operation.setSecurity(Collections.emptyList());
			}
			return operation;
		};
	}

}