package com.example.device.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "devices")
public class Device {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome è obbligatorio")
    private String name;
    
    @NotNull(message = "Tipo dispositivo è obbligatorio")
    @Enumerated(EnumType.STRING)
    private DeviceType type;
    
    @NotNull(message = "Stato dispositivo è obbligatorio")
    @Enumerated(EnumType.STRING)
    private DeviceStatus status = DeviceStatus.AVAILABLE;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee assignedTo;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }
} 