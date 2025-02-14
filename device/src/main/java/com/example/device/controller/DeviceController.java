package com.example.device.controller;

import com.example.device.dto.DeviceDTO;
import com.example.device.model.Device;
import com.example.device.model.DeviceStatus;
import com.example.device.service.DeviceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    
    @Autowired
    private DeviceService deviceService;
    
    @GetMapping
    public List<Device> getAllDevices() {
        return deviceService.getAllDevices();
    }
    
    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }
    
    @GetMapping("/status/{status}")
    public List<Device> getDevicesByStatus(@PathVariable DeviceStatus status) {
        return deviceService.getDevicesByStatus(status);
    }
    
    @GetMapping("/employee/{employeeId}")
    public List<Device> getDevicesByEmployee(@PathVariable Long employeeId) {
        return deviceService.getDevicesByEmployee(employeeId);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Device createDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        return deviceService.createDevice(deviceDTO);
    }
    
    @PutMapping("/{id}")
    public Device updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceDTO deviceDTO) {
        return deviceService.updateDevice(id, deviceDTO);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
    }
    
    @PostMapping("/{deviceId}/assign/{employeeId}")
    public Device assignDevice(@PathVariable Long deviceId, @PathVariable Long employeeId) {
        return deviceService.assignDeviceToEmployee(deviceId, employeeId);
    }
    
    @PostMapping("/{deviceId}/unassign")
    public Device unassignDevice(@PathVariable Long deviceId) {
        return deviceService.unassignDevice(deviceId);
    }
} 