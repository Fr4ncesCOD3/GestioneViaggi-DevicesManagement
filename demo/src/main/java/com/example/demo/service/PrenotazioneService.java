package com.example.demo.service;

import com.example.demo.dto.PrenotazioneRequest;
import com.example.demo.exception.PrenotazioneConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Dipendente;
import com.example.demo.model.Prenotazione;
import com.example.demo.model.Viaggio;
import com.example.demo.repository.DipendenteRepository;
import com.example.demo.repository.PrenotazioneRepository;
import com.example.demo.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrenotazioneService {
    
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    
    @Autowired
    private DipendenteRepository dipendenteRepository;
    
    @Autowired
    private ViaggioRepository viaggioRepository;
    
    @Transactional
    public Prenotazione creaPrenotazione(PrenotazioneRequest request) {
        Dipendente dipendente = dipendenteRepository.findById(request.getDipendenteId())
            .orElseThrow(() -> new ResourceNotFoundException("Dipendente non trovato"));
            
        Viaggio viaggio = viaggioRepository.findById(request.getViaggioId())
            .orElseThrow(() -> new ResourceNotFoundException("Viaggio non trovato"));
            
        if (prenotazioneRepository.existsByDipendenteAndData(dipendente, viaggio.getData())) {
            throw new PrenotazioneConflictException(
                "Il dipendente ha già una prenotazione per la data " + viaggio.getData());
        }
        
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);
        prenotazione.setNote(request.getNote());
        
        return prenotazioneRepository.save(prenotazione);
    }
    
    public List<Prenotazione> getPrenotazioniByDipendente(Long dipendenteId) {
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
            .orElseThrow(() -> new ResourceNotFoundException("Dipendente non trovato"));
        return prenotazioneRepository.findByDipendente(dipendente);
    }
} 