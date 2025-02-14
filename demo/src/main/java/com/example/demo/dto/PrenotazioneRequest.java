package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public class PrenotazioneRequest {
    @NotNull(message = "Il viaggio ID è obbligatorio")
    private Long viaggioId;
    
    @NotNull(message = "Il dipendente ID è obbligatorio")
    private Long dipendenteId;
    
    private String note;

    // Getter e Setter
    public Long getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(Long viaggioId) {
        this.viaggioId = viaggioId;
    }

    public Long getDipendenteId() {
        return dipendenteId;
    }

    public void setDipendenteId(Long dipendenteId) {
        this.dipendenteId = dipendenteId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
} 