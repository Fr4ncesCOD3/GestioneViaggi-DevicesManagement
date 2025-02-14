package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Viaggio viaggio;
    
    @ManyToOne
    private Dipendente dipendente;
    
    private LocalDate dataRichiesta;
    private String note;
} 