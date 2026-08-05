package com.kailaselectrical.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			
			.authorizeHttpRequests(auth -> auth
					
					.requestMatchers("/api/auth/**").permitAll()
					
					.requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
					
					.requestMatchers(HttpMethod.POST, "/api/bookings").hasRole("CUSTOMER")
					
					.requestMatchers("/api/my/**").hasRole("CUSTOMER")
					
					.requestMatchers("/api/customers/**").hasRole("ADMIN")
					
					.requestMatchers("/api/invoices/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.POST, "/api/services/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.PUT, "/api/services/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")
					
					.requestMatchers("/api/dashboard/**").hasRole("ADMIN")
					
					.requestMatchers("/api/profile/**").hasAnyRole("ADMIN", "CUSTOMER")
					
					.requestMatchers("/api/admin/**").hasRole("ADMIN")
					
					.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
					
					.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
