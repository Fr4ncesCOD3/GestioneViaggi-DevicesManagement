// Questo è il package che contiene l'interfaccia repository
package com.example.demo.repository;

// Importiamo la classe del modello Dipendente che vogliamo gestire
import com.example.demo.model.Dipendente;
// Importiamo l'interfaccia JpaRepository di Spring Data JPA che fornisce metodi CRUD predefiniti
import org.springframework.data.jpa.repository.JpaRepository;

// Questa è l'interfaccia repository per gestire le operazioni sul database per l'entità Dipendente
// Estende JpaRepository che fornisce metodi come save(), findAll(), findById(), delete() ecc.
// I parametri generici sono:
// - Dipendente: il tipo dell'entità da gestire
// - Long: il tipo dell'ID dell'entità
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {
} 