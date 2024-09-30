package com.microservices.restaurant_ms.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.microservices.restaurant_ms.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  @Autowired
  private JwtService jwtService;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {

    String headerAuthorization = request.getHeader("Authorization");

    if(headerAuthorization != null) {
      String tokenExtractedFromHeader = headerAuthorization.replace("Bearer ", "");

      DecodedJWT decodedJWT = this.jwtService.validate(tokenExtractedFromHeader);

      if(decodedJWT == null) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;

      }

      var roles = decodedJWT.getClaim("role").asString();

      var grants = Stream.of(roles)
                         .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                         .toList();

      request.setAttribute("user_id", decodedJWT.getSubject());

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        decodedJWT.getSubject(),
        null,
        grants
      );


      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
