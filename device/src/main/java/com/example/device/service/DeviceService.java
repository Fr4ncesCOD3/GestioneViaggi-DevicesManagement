package com.example.device.service;

import com.example.device.dto.DeviceDTO;
import com.example.device.exception.DeviceAssignmentException;
import com.example.device.exception.ResourceNotFoundException;
import com.example.device.model.Device;
import com.example.device.model.DeviceStatus;
import com.example.device.model.Employee;
import com.example.device.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeviceService {
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private EmployeeService employeeService;
    
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo non trovato con id: " + id));
    }
    
    @Transactional
    public Device createDevice(DeviceDTO deviceDTO) {
        Device device = new Device();
        device.setName(deviceDTO.getName());
        device.setType(deviceDTO.getType());
        device.setStatus(DeviceStatus.AVAILABLE);
        
        return deviceRepository.save(device);
    }
    
    @Transactional
    public Device updateDevice(Long id, DeviceDTO deviceDTO) {
        Device device = getDeviceById(id);
        device.setName(deviceDTO.getName());
        device.setType(deviceDTO.getType());
        if (deviceDTO.getStatus() != null) {
            device.setStatus(deviceDTO.getStatus());
        }
        
        return deviceRepository.save(device);
    }
    
    @Transactional
    public void deleteDevice(Long id) {
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo non trovato con id: " + id);
        }
        deviceRepository.deleteById(id);
    }
    
    @Transactional
    public Device assignDeviceToEmployee(Long deviceId, Long employeeId) {
        Device device = getDeviceById(deviceId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        
        if (device.getStatus() != DeviceStatus.AVAILABLE) {
            throw new DeviceAssignmentException("Il dispositivo non è disponibile per l'assegnazione");
        }
        
        device.setAssignedTo(employee);
        device.setStatus(DeviceStatus.ASSIGNED);
        
        return deviceRepository.save(device);
    }
    
    @Transactional
    public Device unassignDevice(Long deviceId) {
        Device device = getDeviceById(deviceId);
        
        if (device.getStatus() != DeviceStatus.ASSIGNED) {
            throw new DeviceAssignmentException("Il dispositivo non è attualmente assegnato");
        }
        
        device.setAssignedTo(null);
        device.setStatus(DeviceStatus.AVAILABLE);
        
        return deviceRepository.save(device);
    }
    
    public List<Device> getDevicesByStatus(DeviceStatus status) {
        return deviceRepository.findByStatus(status);
    }
    
    public List<Device> getDevicesByEmployee(Long employeeId) {
        return deviceRepository.findByAssignedToId(employeeId);
    }
} 