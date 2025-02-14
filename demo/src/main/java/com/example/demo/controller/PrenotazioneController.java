package com.example.demo.controller;

import com.example.demo.dto.PrenotazioneRequest;
import com.example.demo.model.Prenotazione;
import com.example.demo.service.PrenotazioneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    @PostMapping
    public ResponseEntity<Prenotazione> creaPrenotazione(@Valid @RequestBody PrenotazioneRequest request) {
        Prenotazione prenotazione = prenotazioneService.creaPrenotazione(request);
        return new ResponseEntity<>(prenotazione, HttpStatus.CREATED);
    }
    
    @GetMapping("/dipendente/{dipendenteId}")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniByDipendente(@PathVariable Long dipendenteId) {
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniByDipendente(dipendenteId);
        return ResponseEntity.ok(prenotazioni);
    }
} 