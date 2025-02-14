package com.example.demo.repository;

import com.example.demo.model.Prenotazione;
import com.example.demo.model.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByDipendenteAndDataRichiesta(Dipendente dipendente, LocalDate dataRichiesta);
} 