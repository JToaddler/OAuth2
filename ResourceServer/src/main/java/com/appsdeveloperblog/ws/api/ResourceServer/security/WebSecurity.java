package com.appsdeveloperblog.ws.api.ResourceServer.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurity {

	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
			"/api/test/**", "/authenticate" };
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

		http.authorizeHttpRequests(authz -> authz
				// .antMatchers(HttpMethod.GET, "/test").hasAnyAuthority("ROLE_USER")
				// .antMatchers(HttpMethod.GET, "/test").hasAnyAuthority("ROLE_user")
				.requestMatchers(WHITE_LIST_URL).permitAll()
				.requestMatchers(HttpMethod.GET, "/users/status/check") 
				.hasRole("developer")
				.anyRequest().authenticated()).oauth2ResourceServer(
						oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));


		return http.build();
	}

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
          .requestMatchers(WHITE_LIST_URL);
    }
	
}

//	private JwtAuthenticationConverter jwtAuthenticationConverter() {
//	    // create a custom JWT converter to map the "roles" from the token as granted authorities
//	    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//	    jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
//	    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//	    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//	    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//	    return jwtAuthenticationConverter;
//	  }
