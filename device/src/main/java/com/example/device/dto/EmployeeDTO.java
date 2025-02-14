package com.example.device.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmployeeDTO {
    @NotBlank(message = "Username è obbligatorio")
    private String username;
    
    @NotBlank(message = "Nome è obbligatorio")
    private String firstName;
    
    @NotBlank(message = "Cognome è obbligatorio")
    private String lastName;
    
    @Email(message = "Email non valida")
    @NotBlank(message = "Email è obbligatoria")
    private String email;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 