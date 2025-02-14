package com.example.demo.controller;

import com.example.demo.model.Viaggio;
import com.example.demo.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/viaggi")
public class ViaggioController {
    
    @Autowired
    private ViaggioRepository viaggioRepository;
    
    @GetMapping
    public List<Viaggio> getAllViaggi() {
        return viaggioRepository.findAll();
    }
    
    @PostMapping
    public Viaggio createViaggio(@RequestBody Viaggio viaggio) {
        return viaggioRepository.save(viaggio);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Viaggio> getViaggioById(@PathVariable Long id) {
        return viaggioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Viaggio> updateViaggio(@PathVariable Long id, @RequestBody Viaggio viaggioDetails) {
        return viaggioRepository.findById(id)
                .map(viaggio -> {
                    viaggio.setDestinazione(viaggioDetails.getDestinazione());
                    viaggio.setData(viaggioDetails.getData());
                    viaggio.setStato(viaggioDetails.getStato());
                    Viaggio updatedViaggio = viaggioRepository.save(viaggio);
                    return ResponseEntity.ok(updatedViaggio);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViaggio(@PathVariable Long id) {
        return viaggioRepository.findById(id)
                .map(viaggio -> {
                    viaggioRepository.delete(viaggio);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 