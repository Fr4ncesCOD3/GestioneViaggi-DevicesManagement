package com.example.demo.security;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Service
public class RateLimitingService {
    private final Map<String, LoginAttempts> loginAttemptsMap = new ConcurrentHashMap<>();
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    // Configurazione rate limiting
    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 15;
    private static final int TOKENS_PER_MINUTE = 10;

    public boolean isBlocked(String ipAddress) {
        LoginAttempts attempts = loginAttemptsMap.get(ipAddress);
        if (attempts != null && attempts.isBlocked()) {
            if (attempts.canReset()) {
                loginAttemptsMap.remove(ipAddress);
                return false;
            }
            return true;
        }
        return false;
    }

    public void recordFailedAttempt(String ipAddress) {
        loginAttemptsMap.compute(ipAddress, (key, attempts) -> {
            if (attempts == null) {
                attempts = new LoginAttempts();
            }
            attempts.incrementAttempts();
            return attempts;
        });
    }

    public void resetAttempts(String ipAddress) {
        loginAttemptsMap.remove(ipAddress);
    }

    public boolean tryConsume(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, this::createNewBucket);
        return bucket.tryConsume(1);
    }

    private Bucket createNewBucket(String ipAddress) {
        Bandwidth limit = Bandwidth.builder()
            .capacity(TOKENS_PER_MINUTE)
            .refillIntervally(TOKENS_PER_MINUTE, Duration.ofMinutes(1))
            .build();
            
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    private static class LoginAttempts {
        private int attempts;
        private long blockStartTime;

        public void incrementAttempts() {
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                blockStartTime = System.currentTimeMillis();
            }
        }

        public boolean isBlocked() {
            return attempts >= MAX_ATTEMPTS;
        }

        public boolean canReset() {
            return System.currentTimeMillis() - blockStartTime > Duration.ofMinutes(BLOCK_DURATION_MINUTES).toMillis();
        }
    }
} 