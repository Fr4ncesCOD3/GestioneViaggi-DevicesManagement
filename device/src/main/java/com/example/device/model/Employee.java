package com.example.device.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username è obbligatorio")
    @Column(unique = true)
    private String username;
    
    @NotBlank(message = "Nome è obbligatorio")
    private String firstName;
    
    @NotBlank(message = "Cognome è obbligatorio")
    private String lastName;
    
    @Email(message = "Email non valida")
    @NotBlank(message = "Email è obbligatoria")
    @Column(unique = true)
    private String email;
} 