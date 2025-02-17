package com.example.demo.security;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        // Ignora le richieste a Swagger e OpenAPI
        String path = request.getRequestURI();
        if (isSwaggerOrOpenApiRequest(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Applica rate limiting solo per il login e altre richieste protette
        if (isLoginRequest(path)) {
            String ipAddress = getClientIP(request);
            
            // Verifica se l'IP è bloccato
            if (rateLimitingService.isBlocked(ipAddress)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Troppi tentativi di accesso. Riprova più tardi.");
                return;
            }

            // Verifica il rate limiting
            if (!rateLimitingService.tryConsume(ipAddress)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Troppe richieste. Riprova tra qualche minuto.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isSwaggerOrOpenApiRequest(String path) {
        return path.startsWith("/swagger-ui") || 
               path.startsWith("/v3/api-docs") || 
               path.equals("/swagger-ui.html");
    }

    private boolean isLoginRequest(String path) {
        return path.equals("/api/auth/login");
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
} 