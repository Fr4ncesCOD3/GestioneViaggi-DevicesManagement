package com.example.demo.repository;

import com.example.demo.model.Dipendente;
import com.example.demo.model.Prenotazione;
import com.example.demo.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Prenotazione p " +
           "WHERE p.dipendente = :dipendente AND p.viaggio.data = :data")
    boolean existsByDipendenteAndData(Dipendente dipendente, LocalDate data);
    
    List<Prenotazione> findByDipendente(Dipendente dipendente);
    List<Prenotazione> findByViaggio(Viaggio viaggio);
} 