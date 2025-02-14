package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Viaggio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String destinazione;
    private LocalDate data;
    
    @Enumerated(EnumType.STRING)
    private StatoViaggio stato;
} 