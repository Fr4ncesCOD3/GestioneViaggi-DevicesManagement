// Package che contiene le classi di configurazione per la sicurezza
package com.example.demo.security;

// Import necessari per le funzionalità di sicurezza e accesso al repository
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.repository.DipendenteRepository;

import lombok.RequiredArgsConstructor;

// Indica che questa è una classe di configurazione Spring
@Configuration
// Genera un costruttore con i campi final
@RequiredArgsConstructor
public class ApplicationConfig {

    // Repository per accedere ai dati dei dipendenti
    private final DipendenteRepository repository;

    // Bean per il servizio che carica i dettagli utente
    @Bean
    public UserDetailsService userDetailsService() {
        // Cerca l'utente per email, lancia eccezione se non trovato
        return username -> repository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));
    }

    // Bean per configurare il provider di autenticazione
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Crea un nuovo provider di autenticazione DAO
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Imposta il servizio per caricare i dettagli utente
        authProvider.setUserDetailsService(userDetailsService());
        // Imposta l'encoder per le password
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Bean per gestire l'autenticazione
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Bean per codificare le password
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usa BCrypt per l'hashing delle password
        return new BCryptPasswordEncoder();
    }
} 