// Questo è il package che contiene la classe per la validazione dei dati
package com.example.demo.validation;

// Importiamo le classi necessarie per il nostro validatore
import com.example.demo.model.Dipendente;
import com.example.demo.model.Viaggio;
import java.time.LocalDate;
import java.util.regex.Pattern;

// Questa classe contiene metodi per validare i dati inseriti dagli utenti
public class DataValidator {
    // Definiamo un pattern (schema) per validare gli indirizzi email
    // Il pattern verifica che l'email contenga caratteri validi e un @ seguito da un dominio
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    
    // Pattern per validare gli username
    // Permette solo lettere, numeri e alcuni caratteri speciali (., _, -) 
    // La lunghezza deve essere tra 3 e 20 caratteri
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9._-]{3,20}$");

    // Metodo per validare i dati di un viaggio
    public static void validateViaggio(String destinazione, LocalDate data) {
        // Verifica che la destinazione non sia vuota o solo spazi
        if (destinazione == null || destinazione.trim().isEmpty()) {
            throw new ValidationException("La destinazione non può essere vuota");
        }
        // Controlla che la lunghezza della destinazione sia appropriata
        if (destinazione.length() < 2 || destinazione.length() > 100) {
            throw new ValidationException("La destinazione deve essere tra 2 e 100 caratteri");
        }
        // Verifica che sia stata specificata una data
        if (data == null) {
            throw new ValidationException("La data non può essere vuota");
        }
        // Controlla che la data non sia nel passato
        if (data.isBefore(LocalDate.now())) {
            throw new ValidationException("La data non può essere nel passato");
        }
    }

    // Metodo per validare i dati di un dipendente
    public static void validateDipendente(String username, String nome, String cognome, String email) {
        // Verifica che l'username rispetti il pattern definito sopra
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ValidationException("Username non valido (usa solo lettere, numeri, ., _ o - e tra 3-20 caratteri)");
        }
        // Controlla che il nome sia valido e di lunghezza appropriata
        if (nome == null || nome.trim().isEmpty() || nome.length() < 2 || nome.length() > 50) {
            throw new ValidationException("Il nome deve essere tra 2 e 50 caratteri");
        }
        // Controlla che il cognome sia valido e di lunghezza appropriata
        if (cognome == null || cognome.trim().isEmpty() || cognome.length() < 2 || cognome.length() > 50) {
            throw new ValidationException("Il cognome deve essere tra 2 e 50 caratteri");
        }
        // Verifica che l'email rispetti il pattern definito sopra
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Email non valida");
        }
    }
} 