// Package che contiene le classi di sicurezza dell'applicazione
package com.example.demo.security;

// Import necessari per le funzionalità di I/O e sicurezza
import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// Indica che questa classe è un componente Spring
@Component
// Genera un costruttore con i campi final
@RequiredArgsConstructor
// Filtro che viene eseguito una volta per ogni richiesta HTTP
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servizio per gestire le operazioni sui JWT
    private final JwtService jwtService;
    // Servizio per caricare i dettagli dell'utente
    private final UserDetailsService userDetailsService;

    // Metodo che implementa la logica del filtro
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Estrae l'header di autorizzazione dalla richiesta
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se non c'è header di autorizzazione o non inizia con "Bearer ", passa al prossimo filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Estrae il token JWT dall'header (rimuovendo "Bearer ")
        jwt = authHeader.substring(7);
        // Estrae l'email dell'utente dal token
        userEmail = jwtService.extractUsername(jwt);

        // Se l'email è presente e l'utente non è già autenticato
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carica i dettagli dell'utente dal database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            
            // Verifica se il token è valido per l'utente
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Crea un token di autenticazione
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );
                
                // Aggiunge i dettagli della richiesta al token
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                
                // Imposta l'autenticazione nel contesto di sicurezza
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Passa al prossimo filtro nella catena
        filterChain.doFilter(request, response);
    }
} 