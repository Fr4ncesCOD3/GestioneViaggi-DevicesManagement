// Questo è il package che contiene la classe
package com.example.demo.model;

// Importiamo le librerie necessarie
// jakarta.persistence contiene le annotazioni per mappare la classe al database
import jakarta.persistence.*;
// lombok fornisce annotazioni per generare automaticamente getter, setter e costruttori
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
// Importiamo la classe per gestire le date
import java.time.LocalDate;

// @Entity indica che questa classe rappresenta una tabella nel database
@Entity
// @Getter e @Setter generano automaticamente i metodi get e set per tutti i campi
@Getter
@Setter
// @NoArgsConstructor crea un costruttore vuoto
@NoArgsConstructor
// @AllArgsConstructor crea un costruttore con tutti i campi
@AllArgsConstructor
public class Prenotazione {
    // @Id indica che questo è il campo chiave primaria
    @Id
    // @GeneratedValue fa generare automaticamente l'ID dal database
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @ManyToOne indica una relazione molti-a-uno con la classe Viaggio
    // optional = false significa che ogni prenotazione deve avere un viaggio
    @ManyToOne(optional = false)
    // @JoinColumn specifica la colonna che contiene la chiave esterna
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;
    
    // Simile al campo viaggio, ma per il dipendente
    // Una prenotazione appartiene a un dipendente, e un dipendente può avere molte prenotazioni
    @ManyToOne(optional = false)
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;
    
    // @Column personalizza come il campo viene salvato nel database
    // nullable = false significa che il campo non può essere vuoto
    @Column(nullable = false)
    private LocalDate dataRichiesta;
    
    // Campo per eventuali note sulla prenotazione
    // Può essere null perché non ha annotazioni di vincolo
    private String note;
} 