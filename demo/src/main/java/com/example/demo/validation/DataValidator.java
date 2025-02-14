package com.example.demo.validation;

import com.example.demo.model.Dipendente;
import com.example.demo.model.Viaggio;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class DataValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._-]{3,20}$");

    public static void validateViaggio(String destinazione, LocalDate data) {
        if (destinazione == null || destinazione.trim().isEmpty()) {
            throw new ValidationException("La destinazione non può essere vuota");
        }
        if (destinazione.length() < 2 || destinazione.length() > 100) {
            throw new ValidationException("La destinazione deve essere tra 2 e 100 caratteri");
        }
        if (data == null) {
            throw new ValidationException("La data non può essere vuota");
        }
        if (data.isBefore(LocalDate.now())) {
            throw new ValidationException("La data non può essere nel passato");
        }
    }

    public static void validateDipendente(String username, String nome, String cognome, String email) {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ValidationException("Username non valido (usa solo lettere, numeri, ., _ o - e tra 3-20 caratteri)");
        }
        if (nome == null || nome.trim().isEmpty() || nome.length() < 2 || nome.length() > 50) {
            throw new ValidationException("Il nome deve essere tra 2 e 50 caratteri");
        }
        if (cognome == null || cognome.trim().isEmpty() || cognome.length() < 2 || cognome.length() > 50) {
            throw new ValidationException("Il cognome deve essere tra 2 e 50 caratteri");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Email non valida");
        }
    }
} 