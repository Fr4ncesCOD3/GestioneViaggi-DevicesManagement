// Package che contiene le classi di sicurezza dell'applicazione
package com.example.demo.security;

// Import necessari per le funzionalità di sicurezza
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// Indica che questa è una classe di configurazione Spring
@Configuration
// Abilita la sicurezza web di Spring Security
@EnableWebSecurity
// Genera un costruttore con i campi final
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro per l'autenticazione JWT
    private final JwtAuthenticationFilter jwtAuthFilter;
    // Provider per l'autenticazione
    private final AuthenticationProvider authenticationProvider;
    // Filtro per il rate limiting
    private final RateLimitingFilter rateLimitingFilter;

    // Bean per configurare la catena di filtri di sicurezza
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabilita la protezione CSRF poiché usiamo JWT
            .csrf(csrf -> csrf.disable())
            // Configura le intestazioni di sicurezza HTTP
            .headers(headers -> headers
                // Nega l'inclusione in frame per prevenire attacchi clickjacking
                .frameOptions(frame -> frame.deny())
                // Disabilita la protezione XSS del browser (gestita da CSP)
                .xssProtection(xss -> xss.disable())
                // Imposta la Content Security Policy
                .contentSecurityPolicy(csp -> 
                    csp.policyDirectives("default-src 'self'; frame-ancestors 'none'; script-src 'self'"))
            )
            // Configura le regole di autorizzazione per le richieste HTTP
            .authorizeHttpRequests(auth -> auth
                // Permette accesso pubblico alle API di autenticazione
                .requestMatchers("/api/auth/**").permitAll()
                // Permette accesso pubblico alla documentazione Swagger/OpenAPI
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                // Richiede autenticazione per tutte le altre richieste
                .anyRequest().authenticated()
            )
            // Configura la gestione delle sessioni
            .sessionManagement(session -> session
                // Imposta la policy delle sessioni come STATELESS per JWT
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Configura il provider di autenticazione
            .authenticationProvider(authenticationProvider)
            // Aggiunge i filtri personalizzati prima del filtro standard
            .addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 