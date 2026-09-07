package com.kailaselectrical.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			
			.csrf(csrf -> csrf.disable())
			
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			.authorizeHttpRequests(auth -> auth
					
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					
					.requestMatchers(
						    "/api/auth/login",
						    "/api/auth/register",
						    "/api/auth/forgot-password",
						    "/api/auth/reset-password"
						).permitAll()
					
					.requestMatchers("/swagger-ui/**", "swagger-ui.html", "v3/api/docs/**").permitAll()
					
					.requestMatchers(HttpMethod.GET, "/api/services/**").permitAll()
					
					.requestMatchers(HttpMethod.POST, "/api/bookings").hasRole("CUSTOMER")
					
					.requestMatchers(HttpMethod.GET, "/api/bookings/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.PATCH, "/api/bookings/**").hasRole("ADMIN")
					
					.requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
					
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
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(
				List.of(
						"http://localhost:5173", 
						"https://kailas-electrical-ui.vercel.app"
						)
				);
		
		configuration.setAllowedMethods(
				List.of(
						"GET",
						"POST",
						"PUT",
						"PATCH",
						"DELETE",
						"OPTIONS"
				)
		);
		
		configuration.setAllowedHeaders(List.of("*"));
		
		configuration.setExposedHeaders(List.of("Authorization"));
		
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}
}
