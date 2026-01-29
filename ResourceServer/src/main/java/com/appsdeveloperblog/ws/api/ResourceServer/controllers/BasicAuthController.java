package com.appsdeveloperblog.ws.api.ResourceServer.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "basicAuth")
@RestController
public class BasicAuthController {

	@GetMapping("/getToken")
	public Jwt getToken(@AuthenticationPrincipal Jwt jwt) {

		return jwt;
	}

}
