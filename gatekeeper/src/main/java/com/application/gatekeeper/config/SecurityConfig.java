package com.application.gatekeeper.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/register", "/api/login", "/api/entry").permitAll()
                        .requestMatchers("/api/gatekeeper/*", "/api/gatekeeper/*/*").hasAuthority("ROLE_GATEKEEPER")
                        .requestMatchers("/api/admin/*", "/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/resident/*", "/api/resident/*/*").hasAuthority("ROLE_RESIDENT")
                        .anyRequest().authenticated()
        );
        httpSecurity.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return httpSecurity.build();
    }

}
