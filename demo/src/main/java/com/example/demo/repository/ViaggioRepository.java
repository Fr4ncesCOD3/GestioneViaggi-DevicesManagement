// Questo è il package che contiene l'interfaccia repository
package com.example.demo.repository;

// Importiamo la classe del modello Viaggio che vogliamo gestire
import com.example.demo.model.Viaggio;
// Importiamo l'interfaccia JpaRepository di Spring che fornisce metodi CRUD predefiniti
import org.springframework.data.jpa.repository.JpaRepository;

// Questa è l'interfaccia repository per gestire le operazioni sul database per l'entità Viaggio
// Estende JpaRepository che fornisce metodi come save(), findAll(), findById(), delete() ecc.
// I parametri generici sono:
// - Viaggio: il tipo dell'entità da gestire 
// - Long: il tipo dell'ID dell'entità
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {
} 