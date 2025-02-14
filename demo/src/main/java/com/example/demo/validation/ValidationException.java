// Questo è il package che contiene la classe per la gestione delle eccezioni di validazione
package com.example.demo.validation;

// Questa classe definisce un tipo personalizzato di eccezione per gli errori di validazione
// Estende RuntimeException, che è un tipo di eccezione che non richiede di essere gestita esplicitamente
public class ValidationException extends RuntimeException {
    // Questo è il costruttore della classe che accetta un messaggio di errore
    // Quando creiamo una nuova ValidationException, passiamo il messaggio che descrive l'errore
    // super(message) passa il messaggio al costruttore della classe padre (RuntimeException)
    public ValidationException(String message) {
        super(message);
    }
} 