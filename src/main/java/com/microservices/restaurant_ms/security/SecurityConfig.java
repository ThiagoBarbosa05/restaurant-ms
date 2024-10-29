package com.microservices.restaurant_ms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf(csrf -> csrf.disable());

    httpSecurity.authorizeHttpRequests(auth -> {
      auth.requestMatchers(HttpMethod.GET,"/api/dishes/**").permitAll();
      auth.anyRequest().authenticated();
    }).addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class);

    return httpSecurity.build();
  }
}
