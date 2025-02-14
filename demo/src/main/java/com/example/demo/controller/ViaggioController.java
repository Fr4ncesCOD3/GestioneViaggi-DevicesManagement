package com.example.demo.controller;

import com.example.demo.model.StatoViaggio;
import com.example.demo.model.Viaggio;
import com.example.demo.repository.ViaggioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public ResponseEntity<Viaggio> createViaggio(@Valid @RequestBody ViaggioRequest request) {
        log.info("Creazione nuovo viaggio per destinazione: {}", request.getDestinazione());
        Viaggio viaggio = new Viaggio();
        viaggio.setDestinazione(request.getDestinazione());
        viaggio.setData(request.getData());
        viaggio.setStato(StatoViaggio.IN_PROGRAMMA);
        return ResponseEntity.ok(viaggioRepository.save(viaggio));
    }

    @PutMapping("/{id}/stato")
    public ResponseEntity<Viaggio> updateStatoViaggio(@PathVariable Long id, @RequestBody StatoViaggio stato) {
        return viaggioRepository.findById(id)
                .map(viaggio -> {
                    viaggio.setStato(stato);
                    return ResponseEntity.ok(viaggioRepository.save(viaggio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaggio> getViaggio(@PathVariable Long id) {
        log.info("Ricerca viaggio con ID: {}", id);
        return viaggioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 