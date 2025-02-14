// Questo è il package che contiene il controller
package com.example.demo.controller;

// Importiamo le classi necessarie
import com.example.demo.model.Viaggio;
import com.example.demo.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// Questa annotazione indica che è un controller REST che gestirà richieste HTTP
@RestController
// Questa è la base URL per tutte le richieste gestite da questo controller
@RequestMapping("/api/viaggi")
public class ViaggioController {
    
    // Iniettiamo automaticamente il repository che gestirà le operazioni sul database
    @Autowired
    private ViaggioRepository viaggioRepository;
    
    // Gestisce richieste GET per ottenere tutti i viaggi
    @GetMapping
    public List<Viaggio> getAllViaggi() {
        return viaggioRepository.findAll(); // Restituisce tutti i viaggi dal database
    }
    
    // Gestisce richieste POST per creare un nuovo viaggio
    @PostMapping
    public Viaggio createViaggio(@RequestBody Viaggio viaggio) {
        return viaggioRepository.save(viaggio); // Salva il nuovo viaggio nel database
    }
    
    // Gestisce richieste GET per ottenere un viaggio specifico tramite ID
    @GetMapping("/{id}")
    public ResponseEntity<Viaggio> getViaggioById(@PathVariable Long id) {
        return viaggioRepository.findById(id) // Cerca il viaggio per ID
                .map(ResponseEntity::ok) // Se trovato, restituisce 200 OK con il viaggio
                .orElse(ResponseEntity.notFound().build()); // Se non trovato, restituisce 404 Not Found
    }
    
    // Gestisce richieste PUT per aggiornare un viaggio esistente
    @PutMapping("/{id}")
    public ResponseEntity<Viaggio> updateViaggio(@PathVariable Long id, @RequestBody Viaggio viaggioDetails) {
        return viaggioRepository.findById(id) // Cerca il viaggio da aggiornare
                .map(viaggio -> {
                    // Aggiorna i campi del viaggio con i nuovi dati
                    viaggio.setDestinazione(viaggioDetails.getDestinazione());
                    viaggio.setData(viaggioDetails.getData());
                    viaggio.setStato(viaggioDetails.getStato());
                    Viaggio updatedViaggio = viaggioRepository.save(viaggio); // Salva le modifiche
                    return ResponseEntity.ok(updatedViaggio); // Restituisce 200 OK con il viaggio aggiornato
                })
                .orElse(ResponseEntity.notFound().build()); // Se non trovato, restituisce 404 Not Found
    }
    
    // Gestisce richieste DELETE per eliminare un viaggio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViaggio(@PathVariable Long id) {
        return viaggioRepository.findById(id) // Cerca il viaggio da eliminare
                .map(viaggio -> {
                    viaggioRepository.delete(viaggio); // Elimina il viaggio
                    return ResponseEntity.ok().<Void>build(); // Restituisce 200 OK
                })
                .orElse(ResponseEntity.notFound().build()); // Se non trovato, restituisce 404 Not Found
    }
} 