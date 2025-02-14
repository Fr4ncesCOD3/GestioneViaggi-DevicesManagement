package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import java.time.LocalDate;

public class ViaggioRequest {
    @NotNull(message = "La destinazione è obbligatoria")
    private String destinazione;
    
    @NotNull(message = "La data è obbligatoria")
    @Future(message = "La data deve essere futura")
    private LocalDate data;
    
    // getters, setters
} 