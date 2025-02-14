package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "viaggi")
public class Viaggio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String destinazione;
    
    @Column(nullable = false)
    private LocalDate data;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoViaggio stato;

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public StatoViaggio getStato() {
        return stato;
    }

    public void setStato(StatoViaggio stato) {
        this.stato = stato;
    }
} 