package com.kailaselectrical.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	
	private final CustomUserDetailsService userDetailsService;
	
		protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) 
			throws ServletException, IOException{
		
			if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
				
				filterChain.doFilter(request, response);
				
				return;
			}
			
			final String authHeader = request.getHeader("Authorization");
			
			if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
				
				filterChain.doFilter(request, response);
				
				return;
			}
			
			String token = authHeader.substring(7);
			
			String email = jwtService.extractUsername(token);
			
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				
				if (jwtService.isTokenValid(token, userDetails)) {
					
					UsernamePasswordAuthenticationToken authentication = 
							new UsernamePasswordAuthenticationToken(
									userDetails, 
									null, 
									userDetails.getAuthorities());
					
					authentication.setDetails(
							new WebAuthenticationDetailsSource()
							.buildDetails(request));
					
					System.out.println("Authenticated user: " + email);
					System.out.println("Authorities: " + userDetails.getAuthorities());
					
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				}
			}
			filterChain.doFilter(request, response);
		}
}
