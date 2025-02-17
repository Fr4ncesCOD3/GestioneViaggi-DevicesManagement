package com.example.demo.controller;

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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RateLimitingService rateLimitingService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        
        String ipAddress = getClientIP(httpRequest);
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );
            
            UserDetails user = userDetailsService.loadUserByUsername(request.email());
            String jwtToken = jwtService.generateToken(user);
            
            // Reset tentativi falliti dopo login riuscito
            rateLimitingService.resetAttempts(ipAddress);
            
            return ResponseEntity.ok(jwtToken);
            
        } catch (AuthenticationException e) {
            // Registra tentativo fallito
            rateLimitingService.recordFailedAttempt(ipAddress);
            
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Credenziali non valide");
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

record LoginRequest(String email, String password) {} 