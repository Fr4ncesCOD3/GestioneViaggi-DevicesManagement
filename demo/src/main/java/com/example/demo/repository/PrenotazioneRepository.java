// Questo è il package che contiene l'interfaccia repository
package com.example.demo.repository;

// Importiamo le classi necessarie
// La classe del modello Prenotazione che vogliamo gestire
import com.example.demo.model.Prenotazione;
// La classe Dipendente che useremo come parametro di ricerca
import com.example.demo.model.Dipendente;
// L'interfaccia JpaRepository di Spring che fornisce metodi CRUD predefiniti
import org.springframework.data.jpa.repository.JpaRepository;
// Classi per gestire date e liste
import java.time.LocalDate;
import java.util.List;

// Questa è l'interfaccia repository per gestire le operazioni sul database per l'entità Prenotazione
// Estende JpaRepository che fornisce metodi come save(), findAll(), findById(), delete() ecc.
// I parametri generici sono:
// - Prenotazione: il tipo dell'entità da gestire
// - Long: il tipo dell'ID dell'entità
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    // Questo metodo personalizzato cerca le prenotazioni in base a due criteri:
    // - il dipendente che ha fatto la prenotazione
    // - la data della richiesta
    // Restituisce una lista di tutte le prenotazioni che corrispondono a questi criteri
    List<Prenotazione> findByDipendenteAndDataRichiesta(Dipendente dipendente, LocalDate dataRichiesta);
} 