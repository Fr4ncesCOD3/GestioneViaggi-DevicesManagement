// Package del controller per la gestione dell'autenticazione
package com.example.demo.controller;

// Import delle classi necessarie da Spring Framework e altre dipendenze
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.JwtService;
import com.example.demo.security.RateLimitingService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

// Indica che questa classe è un controller REST
@RestController
// Definisce il path base per tutte le API di questo controller
@RequestMapping("/api/auth")
// Lombok genera automaticamente un costruttore con i campi final
@RequiredArgsConstructor
public class AuthController {

    // Dipendenze necessarie per l'autenticazione e la gestione dei token
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RateLimitingService rateLimitingService;

    // Endpoint per il login che accetta richieste POST
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginRequest request, // Corpo della richiesta con credenziali
            HttpServletRequest httpRequest) {
        
        // Ottiene l'indirizzo IP del client
        String ipAddress = getClientIP(httpRequest);
        
        try {
            // Tenta l'autenticazione con le credenziali fornite
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );
            
            // Se l'autenticazione ha successo, carica i dettagli dell'utente
            UserDetails user = userDetailsService.loadUserByUsername(request.email());
            // Genera un nuovo token JWT
            String jwtToken = jwtService.generateToken(user);
            
            // Reset tentativi falliti dopo login riuscito
            rateLimitingService.resetAttempts(ipAddress);
            
            // Restituisce il token JWT nella risposta
            return ResponseEntity.ok(jwtToken);
            
        } catch (AuthenticationException e) {
            // In caso di credenziali errate, registra il tentativo fallito
            rateLimitingService.recordFailedAttempt(ipAddress);
            
            // Restituisce errore 401 Unauthorized
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Credenziali non valide");
        }
    }

    // Metodo helper per ottenere l'IP del client, gestendo anche i proxy
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

// Record per rappresentare la richiesta di login
record LoginRequest(String email, String password) {} 