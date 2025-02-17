// Package che contiene le classi di sicurezza dell'applicazione
package com.example.demo.security;

// Import necessari per le funzionalità di rate limiting
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

// Indica che questa classe è un servizio Spring
@Service
public class RateLimitingService {
    // Mappe thread-safe per tenere traccia dei tentativi di login e dei bucket per IP
    private final Map<String, LoginAttempts> loginAttemptsMap = new ConcurrentHashMap<>();
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    // Costanti di configurazione per il rate limiting
    private static final int MAX_ATTEMPTS = 5; // Numero massimo di tentativi falliti consentiti
    private static final int BLOCK_DURATION_MINUTES = 15; // Durata del blocco in minuti
    private static final int TOKENS_PER_MINUTE = 10; // Numero di richieste consentite al minuto

    // Verifica se un IP è bloccato
    public boolean isBlocked(String ipAddress) {
        LoginAttempts attempts = loginAttemptsMap.get(ipAddress);
        if (attempts != null && attempts.isBlocked()) {
            // Se il periodo di blocco è scaduto, rimuove il blocco
            if (attempts.canReset()) {
                loginAttemptsMap.remove(ipAddress);
                return false;
            }
            return true;
        }
        return false;
    }

    // Registra un tentativo di login fallito
    public void recordFailedAttempt(String ipAddress) {
        loginAttemptsMap.compute(ipAddress, (key, attempts) -> {
            if (attempts == null) {
                attempts = new LoginAttempts();
            }
            attempts.incrementAttempts();
            return attempts;
        });
    }

    // Resetta i tentativi di login per un IP
    public void resetAttempts(String ipAddress) {
        loginAttemptsMap.remove(ipAddress);
    }

    // Verifica se una richiesta può essere elaborata secondo il rate limiting
    public boolean tryConsume(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, this::createNewBucket);
        return bucket.tryConsume(1);
    }

    // Crea un nuovo bucket per il rate limiting
    private Bucket createNewBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.builder()
            .capacity(TOKENS_PER_MINUTE)
            .refillIntervally(TOKENS_PER_MINUTE, Duration.ofMinutes(1))
            .build();
            
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    // Classe interna per gestire i tentativi di login
    private static class LoginAttempts {
        private int attempts; // Numero di tentativi falliti
        private long blockStartTime; // Timestamp di inizio del blocco

        // Incrementa il contatore dei tentativi e imposta il timestamp di blocco se necessario
        public void incrementAttempts() {
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                blockStartTime = System.currentTimeMillis();
            }
        }

        // Verifica se l'IP è bloccato
        public boolean isBlocked() {
            return attempts >= MAX_ATTEMPTS;
        }

        // Verifica se il periodo di blocco è scaduto
        public boolean canReset() {
            return System.currentTimeMillis() - blockStartTime > Duration.ofMinutes(BLOCK_DURATION_MINUTES).toMillis();
        }
    }
} 