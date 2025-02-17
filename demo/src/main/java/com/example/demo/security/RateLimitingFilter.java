// Package che contiene le classi di sicurezza dell'applicazione
package com.example.demo.security;

// Import necessari per le funzionalità di I/O e sicurezza
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

// Indica che questa classe è un componente Spring
@Component
// Specifica l'ordine di esecuzione del filtro (1 significa che viene eseguito per primo)
@Order(1)
// Genera un costruttore con i campi final
@RequiredArgsConstructor
// Filtro che limita il numero di richieste per IP
public class RateLimitingFilter extends OncePerRequestFilter {

    // Servizio che implementa la logica del rate limiting
    private final RateLimitingService rateLimitingService;

    // Metodo principale del filtro che viene eseguito per ogni richiesta
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        // Ottiene il percorso della richiesta
        String path = request.getRequestURI();
        
        // Se la richiesta è per Swagger o OpenAPI, la lascia passare senza controlli
        if (isSwaggerOrOpenApiRequest(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Applica il rate limiting solo per le richieste di login
        if (isLoginRequest(path)) {
            // Ottiene l'indirizzo IP del client
            String ipAddress = getClientIP(request);
            
            // Se l'IP è bloccato, restituisce un errore 429 (Too Many Requests)
            if (rateLimitingService.isBlocked(ipAddress)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Troppi tentativi di accesso. Riprova più tardi.");
                return;
            }

            // Verifica se l'IP ha superato il limite di richieste
            if (!rateLimitingService.tryConsume(ipAddress)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Troppe richieste. Riprova tra qualche minuto.");
                return;
            }
        }

        // Se tutti i controlli passano, procede con la catena dei filtri
        filterChain.doFilter(request, response);
    }

    // Verifica se la richiesta è diretta a Swagger o OpenAPI
    private boolean isSwaggerOrOpenApiRequest(String path) {
        return path.startsWith("/swagger-ui") || 
               path.startsWith("/v3/api-docs") || 
               path.equals("/swagger-ui.html");
    }

    // Verifica se la richiesta è per il login
    private boolean isLoginRequest(String path) {
        return path.equals("/api/auth/login");
    }

    // Ottiene l'IP del client, considerando anche il caso in cui la richiesta passi attraverso un proxy
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
} 