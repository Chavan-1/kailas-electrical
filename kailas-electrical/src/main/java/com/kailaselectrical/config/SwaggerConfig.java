package com.kailaselectrical.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
	
	@Bean
	OpenAPI openAPI() {
		
		final String securitySchemeName = "Bearer Authentication";
		
		return new OpenAPI()
				.info(
						new Info()
								.title("Kailash ElectricalService API")
								.version("1.0")
								.description("""
										REST APIs for Kailash Electrical Service Management System.

                                        Features
                                        • Authentication
                                        • Customer Management
                                        • Service Management
                                        • Booking Management
                                        • Invoice Management
                                        • Dashboard
										""")
								.contact(
										new Contact()
													.name("Aarti Chavan")
													.email("kailaselectrical@gmail.com")))
				.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
				.components(new Components().addSecuritySchemes(securitySchemeName, 
							new SecurityScheme()
												.name(securitySchemeName)
												.type(SecurityScheme.Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT")));
	}
}
