package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;
    
    @Column(nullable = false)
    private LocalDate dataRichiesta;
    
    private String note;
} 