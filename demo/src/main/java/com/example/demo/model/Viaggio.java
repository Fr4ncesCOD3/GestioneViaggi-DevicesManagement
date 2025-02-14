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
public class Viaggio {
    // @Id indica che questo è il campo chiave primaria
    @Id
    // @GeneratedValue fa generare automaticamente l'ID dal database
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Il campo destinazione non può essere vuoto (nullable = false)
    @Column(nullable = false)
    private String destinazione;
    
    // La data del viaggio non può essere vuota
    @Column(nullable = false)
    private LocalDate data;
    
    // @Enumerated indica che questo campo è un'enumerazione
    // EnumType.STRING salva il valore come stringa nel database
    @Enumerated(EnumType.STRING)
    // Lo stato del viaggio non può essere vuoto
    @Column(nullable = false)
    private StatoViaggio stato;
} 