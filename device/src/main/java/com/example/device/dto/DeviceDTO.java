package com.example.device.dto;

import com.example.device.model.DeviceStatus;
import com.example.device.model.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeviceDTO {
    @NotBlank(message = "Nome è obbligatorio")
    private String name;
    
    @NotNull(message = "Tipo dispositivo è obbligatorio")
    private DeviceType type;
    
    private DeviceStatus status;
    private Long assignedToId;

    // Getters and Setters
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

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }
} 